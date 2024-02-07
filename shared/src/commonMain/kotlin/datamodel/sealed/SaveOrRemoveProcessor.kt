package datamodel.sealed

import datamodel.model.PostModel

sealed class SaveOrRemoveProcessor {
    data class Remove(val postModel: PostModel) : SaveOrRemoveProcessor()

    data class Save(val postModel: PostModel) : SaveOrRemoveProcessor()
}
