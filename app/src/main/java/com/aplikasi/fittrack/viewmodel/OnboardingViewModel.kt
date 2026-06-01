import androidx.lifecycle.ViewModel
import com.aplikasi.fittrack.model.OnboardingRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {

    // Variabel penampung untuk setiap slide
    private val _motivation = MutableStateFlow("")
    val motivation: StateFlow<String> = _motivation.asStateFlow()

    private val _goal = MutableStateFlow("")
    val goal: StateFlow<String> = _goal.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _age = MutableStateFlow(0)
    val age: StateFlow<Int> = _age.asStateFlow()

    private val _weight = MutableStateFlow(0f)
    val weight: StateFlow<Float> = _weight.asStateFlow()

    private val _height = MutableStateFlow(0f)
    val height: StateFlow<Float> = _height.asStateFlow()

    private val _activityLevel = MutableStateFlow("")
    val activityLevel: StateFlow<String> = _activityLevel.asStateFlow()

    // Fungsi-fungsi untuk mengupdate data saat user milih di UI
    fun updateMotivation(value: String) { _motivation.value = value }
    fun updateGoal(value: String) { _goal.value = value }
    fun updateGender(value: String) { _gender.value = value }
    fun updateAge(value: Int) { _age.value = value }
    fun updateWeight(value: Float) { _weight.value = value }
    fun updateHeight(value: Float) { _height.value = value }
    fun updateActivityLevel(value: String) { _activityLevel.value = value }

    // Fungsi untuk merangkai semua data menjadi OnboardingRequest siap tembak
    fun buildRequest(): OnboardingRequest {
        return OnboardingRequest(
            motivation = _motivation.value,
            goal = _goal.value,
            gender = _gender.value,
            age = _age.value,
            weight = _weight.value,
            height = _height.value,
            activity_level = _activityLevel.value
        )
    }
}