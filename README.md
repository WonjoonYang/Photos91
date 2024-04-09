### HOW TO COMPILE AND RUN
#
#
# 
# CAUTION: *Please make sure to do this inside **Photos91** directory...*
#
#
# 
### IF $PATH_TO_FX is not set

**To Compile:**
```javac --module-path <path to javafx-sdk lib>  --add-modules javafx.controls,javafx.fxml -d . src/*/*/*.java```

**To Run:**
```java --module-path <path to javafx-sdk lib> --add-modules javafx.controls,javafx.fxml -cp . photos.app.PhotosApp```

### IF $PATH_TO_FX is set

**To Compile:**
```javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -d . src/*/*/*.java```

**To Run:**
```java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp . photos.app.PhotosApp```
