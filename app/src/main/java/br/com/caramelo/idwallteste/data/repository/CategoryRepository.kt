package br.com.caramelo.idwallteste.data.repository

import br.com.caramelo.idwallteste.data.model.entity.DogCategory
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CategoryRepository @Inject constructor() {

    suspend fun categories(): List<DogCategory> {
        return suspendCoroutine {
            val list = DogCategory.values().asList()
            it.resume(list)
        }
    }

}