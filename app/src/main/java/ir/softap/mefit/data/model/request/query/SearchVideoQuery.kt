package ir.softap.mefit.data.model.request.query

class SearchVideoQuery(
    issuerId: Int? = null,
    catsId: List<String>? = null,
    query: String? = null,
    tagsId: List<String>? = null,
    typesId: List<String>? = null,
    page: Int? = null,
    count: Int? = null
) : HashMap<String, Any>() {

    init {
        issuerId?.also { put("issuer_id", it) }
        catsId?.also { put("cats_id", it.joinToString(",")) }
        query?.also { put("q", it) }
        tagsId?.also { put("tags_id", it.joinToString(",")) }
        typesId?.also { put("types_id", it.joinToString(",")) }
        page?.also { put("page", it) }
        count?.also { put("count", it) }
    }

}