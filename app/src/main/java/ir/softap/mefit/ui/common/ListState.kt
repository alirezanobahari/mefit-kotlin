package ir.softap.mefit.ui.common

enum class ListState {
    LOADING, SUCCESS, ERROR, IDLE;

    var itemCount = 0
        private set(value) {
            field = value
        }

    fun withItemCount(itemCount: Int) = this.apply {
        this.itemCount = itemCount
    }

}