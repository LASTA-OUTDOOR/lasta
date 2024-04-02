import androidx.lifecycle.ViewModel
import javax.inject.Inject

class RecentActivities @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {
  val mostRecentActivities = repository.getMostRecentActivities()
}
