
# EasyPreference

𝐓𝐡𝐢𝐬 𝐥𝐢𝐛𝐫𝐚𝐫𝐲 𝐡𝐞𝐥𝐩𝐬 𝐲𝐨𝐮 𝐭𝐨 𝐬𝐚𝐯𝐞 𝐲𝐨𝐮𝐫 𝐚𝐜𝐭𝐢𝐯𝐢𝐭𝐲 𝐨𝐫 𝐟𝐫𝐚𝐠𝐦𝐞𝐧𝐭 𝐬𝐭𝐚𝐭𝐞 𝐩𝐞𝐫𝐬𝐢𝐬𝐭𝐞𝐧𝐭𝐥𝐲. 𝐓𝐡𝐞𝐲 𝐰𝐢𝐥𝐥 𝐛𝐞 𝐬𝐚𝐦𝐞 𝐨𝐧 𝐚𝐩𝐩 𝐫𝐞𝐬𝐭𝐚𝐫𝐭 𝐚𝐬 𝐭𝐡𝐞𝐲 𝐰𝐞𝐫𝐞 𝐛𝐞𝐟𝐨𝐫𝐞, 𝐥𝐢𝐤𝐞 𝐢𝐟 𝐲𝐨𝐮 𝐡𝐚𝐯𝐞 𝐜𝐡𝐞𝐜𝐤𝐞𝐝 𝐬𝐨𝐦𝐞 𝐜𝐡𝐞𝐜𝐤𝐛𝐨𝐱𝐞𝐬 𝐨𝐫 𝐬𝐰𝐢𝐭𝐜𝐡𝐞𝐬 𝐨𝐫 𝐚𝐧𝐲 𝐨𝐭𝐡𝐞𝐫 𝐂𝐨𝐦𝐩𝐨𝐮𝐧𝐝𝐁𝐮𝐭𝐭𝐨𝐧 𝐭𝐡𝐞𝐧, 𝐭𝐡𝐢𝐬 𝐜𝐥𝐚𝐬𝐬 𝐰𝐢𝐥𝐥 𝐬𝐚𝐯𝐞 𝐭𝐡𝐞𝐢𝐫 𝐬𝐭𝐚𝐭𝐞 & 𝐲𝐨𝐮 𝐜𝐚𝐧 𝐫𝐞𝐬𝐭𝐨𝐫𝐞 𝐭𝐡𝐞𝐦 𝐨𝐧 𝐨𝐧𝐂𝐫𝐞𝐚𝐭𝐞() 𝐨𝐟 𝐲𝐨𝐮𝐫 𝐚𝐜𝐭𝐢𝐯𝐢𝐭𝐲 𝐨𝐫 𝐟𝐫𝐚𝐠𝐦𝐞𝐧𝐭. 𝐓𝐡𝐞 𝐜𝐨𝐦𝐩𝐨𝐧𝐞𝐧𝐭𝐬 𝐨𝐟 𝐰𝐡𝐢𝐜𝐡 𝐭𝐡𝐞 𝐬𝐭𝐚𝐭𝐞 𝐚𝐫𝐞 𝐬𝐚𝐯𝐞𝐝 𝐚𝐫𝐞 𝐫𝐞𝐟𝐞𝐫𝐫𝐞𝐝 𝐚𝐬 𝐏𝐫𝐞𝐟𝐞𝐫𝐞𝐧𝐜𝐞.
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
	        implementation 'com.github.ErrorxCode:EasyPreference:1.0.0'
	}
```



  
## Usage / Examples
To save & restore activity's state automatically ( The code which is used in above gif ) :-
```java
public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EasyPreference.restoreActivityState(this);

        // Rest of the code
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyPreference.saveActivityState(this);
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
        EasyPreference.saveFragmentState(view);
        
        // Rest of the code
        
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyPreference.restoreFragmentState(view);
    }
}
```

If you want to exclude some views from saving their state, you can declare `android:tag="exclude"` in that view.

You can also use `EasyPreference.saveViewState(View view);` and `EasyPreference.restoreViewState(View view);` for saving particular view.

## Points to remember

- In The method `EasyPreference.saveViewState(View view);` **the view must be either RadioGroup or CompoundButton**, otherwise, exception is thrown.
- The method `EasyPreference.saveActivityState(this);` or `EasyPreference.saveFragmentState(view);` should be called at the last because any modification made after these methods would not be saved.
- The method `EasyPreference.restoreFragmentState(view);` or `EasyPreference.restoreActivityState(this);` should be called at first, in onCreate() / onCreateView() method. Because after that, if user made any changes in view state (before saving them), then all changes will revert back.



  
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

  
