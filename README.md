dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

 dependencies {
	        implementation 'com.github.2318279444:GetAddress:Tag'
	}


![image](https://github.com/user-attachments/assets/6396559b-9d9d-4615-b8c0-b73b1d24baa0)


 ![image](https://github.com/user-attachments/assets/68f32ffe-38b6-45b2-b452-9491775aa8f7)


        LocationAdressFiexdUtil locationAdressFiexdUtil = new LocationAdressFiexdUtil();
        //                                     上下文,是否展示省,是否展示市,是否展示区 
        locationAdressFiexdUtil.getDate(this,true,true,false);
        locationAdressFiexdUtil.setCityCallBack(city -> {
            Toast.makeText(this, city, Toast.LENGTH_SHORT).show();

        });

