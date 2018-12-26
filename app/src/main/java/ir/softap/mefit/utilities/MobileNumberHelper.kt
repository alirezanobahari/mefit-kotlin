package ir.softap.mefit.utilities

object MobileNumberHelper {

    private val irancellNumberCodes = arrayOf(
        "0901", "0902", "0903", "0905", "0930", "0933", "0935", "0936", "0937", "0938", "0939"
    )

    private val hamrahAvalNumberCodes = arrayOf(
        "0990", "0991", "0919", "0910", "0911", "0912", "0913", "0914", "0915", "0916", "0917", "0918"
    )

    enum class Operator {
        IRANCELL, HAMRAH_AVAL, UNDEFINED
    }

    fun getOperator(phoneNumber: String): Operator {
        if (phoneNumber.length < 4)
            return Operator.UNDEFINED
        val phonePrefix = phoneNumber.subSequence(0, 4)
        return when {
            irancellNumberCodes.contains(phonePrefix) -> Operator.IRANCELL
            hamrahAvalNumberCodes.contains(phonePrefix) -> Operator.HAMRAH_AVAL
            else -> Operator.UNDEFINED
        }
    }

    fun validate(phoneNumber: String): Boolean {
        if (phoneNumber.length < 4)
            return false
        val phonePrefix = phoneNumber.subSequence(0, 4)
        return irancellNumberCodes.contains(phonePrefix) || hamrahAvalNumberCodes.contains(phonePrefix)
    }

}