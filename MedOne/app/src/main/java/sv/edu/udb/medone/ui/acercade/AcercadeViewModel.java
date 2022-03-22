package sv.edu.udb.medone.ui.acercade;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AcercadeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AcercadeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is acerca de fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}