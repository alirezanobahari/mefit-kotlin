package ir.softap.mefit.ui.common.model

import ir.softap.mefit.ui.common.UIState

data class Result<T>(
    val uiState: UIState,
    val t: T
)