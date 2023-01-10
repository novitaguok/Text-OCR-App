package com.example.ocrapp.novita.util

object Constant {
    // Image
    const val RATIO_4_3_VALUE = 4.0 / 3.0
    const val RATIO_16_9_VALUE = 16.0 / 9.0

    // File
    const val IMG_FILE_EXT = ".png"
    const val IMG_FILE_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    const val FILE_DIR_CHILD = "OCR"

    // Navigation
    const val ARG_KEY_URI = "uri"
    const val ARG_KEY_TEXT = "text"

    // Firebase Firestore
    const val COLLECTION_NAME = "ocr"
    const val DOC_TITLE_FIELD_KEY = "title"
    const val DOC_BODY_FIELD_KEY = "body"
}