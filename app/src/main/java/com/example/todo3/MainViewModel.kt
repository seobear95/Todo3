package com.example.todo3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.kotlin.where
import java.util.UUID

class MainViewModel : ViewModel() {

    private var realm = Realm.getDefaultInstance()

    val allTestModels by lazy { MutableLiveData<List<TestModel>>() }

    fun addTestModel(title: String, description: String) {
        realm.executeTransaction {
            val testModel = it.createObject(TestModel::class.java, UUID.randomUUID().toString())
            testModel.date = title
            testModel.description = description
            realm.insertOrUpdate(testModel)
        }
    }

    fun selectAllTestModel() {
        val testModels = realm.where<TestModel>().findAll()
        this.allTestModels.value = realm.copyFromRealm(testModels)
    }

    fun selectToDo(date: String): TestModel? {
        return realm.where<TestModel>().equalTo("date", date).findFirst()
    }

    fun deleteTestModel(id: String) {
        val testModel = realm.where(TestModel::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction {
            testModel?.deleteFromRealm()
        }
    }

    fun updateTestModel(id: String, title: String, description: String) {
        val mTestModel = realm.where(TestModel::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction {
            mTestModel?.date = title
            mTestModel?.description = description
            if (mTestModel != null) {
                realm.insertOrUpdate(mTestModel)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!realm.isClosed) {
            realm.close()
        }
    }
}
