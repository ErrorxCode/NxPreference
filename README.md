# Next Preference library
A new way to create preference screen with normal architecture components like activity and views. You can also use this library to save activity/fragment state persistently. Saving state means that the modifiable views like Spinner,checkbox,radiobutton, etc.. after restart, will be in the same state as they were before. 


## Demo

![Demo](/demo.gif)

The above gif shows that the switch, radiobutton and checkbox were kept as it is even after app restarts. This way you can save your activity states.
## Implimentation

In your project level build.gradle
```groovy
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
In your module level build.gradle (app)
```groovy
dependencies {
	        implementation 'com.github.ErrorxCode:NxPreference:1.5'
	}
```


## Usage/Examples

### Saving states
**Note** : If you want to exclude some views from saving their state, you can declare `android:tag="exclude"` in that view.

#### Manual method
To save & restore activity's state manually :-
```java
public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NxPreference.restoreActivityState(this);

        // Rest of the code
    }

    @Override
    protected void onStop() {
        super.onStop();
        NxPreference.saveActivityState(this);
    }
}
```

In case of fragment :-
```java
public class DemoFragment extends Fragment {
    
    public View view;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_blank, container, false);
        NxPreference.saveFragmentState(view);
        
        // Rest of the code
        
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        NxPreference.restoreFragmentState(view);
    }
}
```
#### Automatic method
To save & restore activity's state automatically :-
```java
public class DemoActivity extends AppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NxPreference.autoSaveState(this);

        // Rest of the code
    }
}
```
This is not possible in case of **Fragment**

#### Saving Listview state
**Note:** If you want to save listview, first declare `android:tag="savable"` in it. Then Make sure that adapter is set to the listview befour you call
`NxPreference.saveActivityState(this)` or `NxPreference.autoSaveState(this)`

Either set adapter to the list before calling ` NxPreference.saveActivityState(this)` state:
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        NxPreference.restoreActivityState(this);

        // Rest of the code
    }
```
or call it in the last:
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list);
        List<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        // Rest of the code...

        NxPreference.restoreActivityState(this);
    }
```

### Preferences
There is currently 1 preference. It is simple preference that can be further extended to make more custom preferences.
If you are making your own preference with that preference class, please share it with others by contributing to this library.

Here is the example of simple preference:
```xml
<com.xcoder.nxpref.Header
    app:heading="General settings"
    android:layout_height="wrap_content"
    app:showDivider="false"
    android:layout_width="match_parent"/>

<com.xcoder.nxpref.Preference 
    android:id="@+id/backupPref"
    android:layout_height="wrap_content"
    android:layout_width="match_parent"
    app:summery="Change your default backup settings"
    app:title="Backup settings"/>
```

## Savable views
Only these views state are saved.

- Switch
- RadioButton
- CheckBox
- CompoundButton
- Edittext
- Spinner
- Listview

## Points to remember
- `NxPreference.restoreActivityState(this)` or `NxPreference.autoSaveState(this)` must be called in **onCreate()** or activity.
- `NxPreference.saveActivityState(this)` can be called anywhere in the last. Best in `onStop()`. Any modification made after calling this will not be saved. 
- In case of listview, `NxPreference.restoreActivityState(this)` or `NxPreference.autoSaveState(this)` must be called after setting adapter in listview. Best in last of onCreate method.



  
## License

```
MIT License

Copyright (c) 2021 Rahil khan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```

  
