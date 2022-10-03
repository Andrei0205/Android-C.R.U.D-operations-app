package com.example.crudapp

class CustomerModel {
    var id = 0
    var name: String? = null
    var age = 0
    var isActive: String? = null


    constructor(id: Int, name: String?, age: Int,isActive: String?) {
        this.id = id
        this.name = name
        this.age = age
        this.isActive = isActive

    }

    constructor() {}


}