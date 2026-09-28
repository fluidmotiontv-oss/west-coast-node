package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ArtistLedger
import com.example.data.Memory
import com.example.data.Quest
import com.example.data.StudioRepository
import com.example.logic.ClockEngine
import com.example.BuildConfig
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StudioViewModel(private val repository: StudioRepository) : ViewModel() {

    private val _mentorChat = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val mentorChat: StateFlow<List<Pair<String, String>>> = _mentorChat.asStateFlow()

    private val _isMentorThinking = MutableStateFlow(false)
    val isMentorThinking: StateFlow<Boolean> = _isMentorThinking.asStateFlow()

    private val _fluidTime = MutableStateFlow(ClockEngine.getCurrentFluidTime())
    val fluidTime: StateFlow<ClockEngine.FluidTime> = _fluidTime.asStateFlow()

    val memory: StateFlow<List<Memory>> = repository.memory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ledgers: StateFlow<List<ArtistLedger>> = repository.ledgers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quests: StateFlow<List<Quest>> = repository.quests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Update time every minute (simulated Fluid Motion Time)
        viewModelScope.launch {
            while (true) {
                _fluidTime.value = ClockEngine.getCurrentFluidTime()
                delay(60000)
            }
        }

        // Initialize some sample quests if empty
        viewModelScope.launch {
            quests.take(1).collect {
                if (it.isEmpty()) {
                    repository.addQuest("Morning Surf", "Capture a 9:16 portrait of the first wave.", 50)
                    repository.addQuest("Drop a Beat", "Create a 75 BPM Dub rhythm.", 30)
                    repository.addQuest("Local Ledger", "Sync your harmony notes with the Port Waikato node.", 100)
                }
            }
        }
    }

    fun completeQuest(quest: Quest) {
        viewModelScope.launch {
            repository.completeQuest(quest)
            repository.addLedger("Creator", quest.rewardNotes.toLong(), 0)
        }
    }

    fun addMemory(key: String, value: String) {
        viewModelScope.launch {
            repository.saveMemory(key, value)
        }
    }

    fun askMentor(message: String) {
        _mentorChat.value += ("User" to message)
        _isMentorThinking.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitClient.geminiApi.generateContent(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = GenerateContentRequest(
                        contents = listOf(Content(parts = listOf(Part(text = message)))),
                        systemInstruction = Content(parts = listOf(Part(text = "You are the Principal Creative Mentor 'Studio AI'. You help young creators at Port Waikato turn ocean sessions into art and music. Use surf slang like 'Stoke level high' and 'rad'. Keep it encouraging and mentor-first.")))
                    )
                )
                val mentorResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Stoke level low... I lost my signal. Catch you on the next set."
                
                _mentorChat.value += ("Mentor" to mentorResponse)
            } catch (e: Exception) {
                _mentorChat.value += ("Mentor" to "Bummer! The connection wiped out. ${e.localizedMessage}")
            } finally {
                _isMentorThinking.value = false
            }
        }
    }
}
