package it.polito.wa2.g13.document_store.dtos

import it.polito.wa2.g13.document_store.data.DocumentFile

data class DocumentFileDTO(
    val metadata: DocumentMetadataDTO,
    val bytes: ByteArray,
    val version: Long,
    val id: Long,
) {
    companion object {
        @JvmStatic
        fun from(document: DocumentFile): DocumentFileDTO = DocumentFileDTO(
            metadata = DocumentMetadataDTO.from(document.metadata),
            bytes = document.file,
            version = document.version,
            id = document.id,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DocumentFileDTO

        if (metadata != other.metadata) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metadata.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
