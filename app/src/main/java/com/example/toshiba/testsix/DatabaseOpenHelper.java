package com.example.toshiba.testsix;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

//SQliteAssetHelper
/**Copyright (C) 2011 readyState Software Ltd
 Copyright (C) 2007 The Android Open Source Project

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. **/

public class DatabaseOpenHelper extends SQLiteAssetHelper  {

    private static final String Database_name = "lexitron_v3.db";
    private static final int Database_version = 1;
    Context context;


    public DatabaseOpenHelper(Context context){
        super(context,Database_name,null,Database_version);
        this.context = context;
    }
}
