package sv.edu.udb.medone.ui.acercade;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AcercadeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AcercadeViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("Aplicación MedOne creada para facilitar la compra en línea de tus productos médicos de tu presencia con una amplia gama de medicamentos y servicios para un mejor cuido de tu salud.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}