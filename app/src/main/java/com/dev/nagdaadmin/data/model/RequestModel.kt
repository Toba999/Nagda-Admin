package com.dev.nagdaadmin.data.model

data class RequestModel(
    val id: String = "",
    val uid: String = "",
    val type: RequestType = RequestType.ACCIDENT,
    val status: RequestStatus = RequestStatus.SENT,
    val location: String = "",
    val details: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
fun RequestStatus.bannerContent(): Pair<String, String> = when (this) {
    RequestStatus.SENT        -> Pair("تم إرسال البلاغ",         "تم إرسال بياناتك وموقعك بنجاح")
    RequestStatus.RECEIVED    -> Pair("تم استلام البلاغ",        "الدفاع المدني اطلع على البلاغ")
    RequestStatus.IN_PROGRESS -> Pair("جاري التعامل مع البلاغ", "تم توجيه الفريق المختصة للموقع")
    RequestStatus.ON_THE_WAY  -> Pair("الفريق في الطريق إليك",  "يرجى الالتزام بتعليمات السلامة")
    RequestStatus.DONE        -> Pair("تم التعامل مع البلاغ",   "لتمنى السلامة للجميع")
    RequestStatus.CANCELLED   -> Pair("تم إلغاء البلاغ",        "تم إلغاء البلاغ بنجاح")
}