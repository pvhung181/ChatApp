package vn.pvhung.appchat.data;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SampleData {
    public static void addSampleDataToFireStore(Context ctx) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("first_name", "Hung");
        data.put("last_name", "Pham");

        database.collection("users")
                .add(data)
                .addOnSuccessListener(v -> Toast.makeText(ctx, "Data inserted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(v -> Toast.makeText(ctx, "Fail to insert", Toast.LENGTH_SHORT).show());
    }
}
