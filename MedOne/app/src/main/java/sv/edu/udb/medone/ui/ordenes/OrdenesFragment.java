package sv.edu.udb.medone.ui.ordenes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.udb.medone.databinding.FragmentOrdenesBinding;
import sv.edu.udb.medone.ui.ordenes.OrdenesViewModel;

public class OrdenesFragment extends Fragment {

    private OrdenesViewModel ordenesViewModel;
    private FragmentOrdenesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ordenesViewModel =
                new ViewModelProvider(this).get(OrdenesViewModel.class);

        binding = FragmentOrdenesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textOrdenes;
        ordenesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}