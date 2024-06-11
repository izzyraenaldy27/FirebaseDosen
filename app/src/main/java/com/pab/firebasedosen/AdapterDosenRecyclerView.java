package com.pab.firebasedosen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class AdapterDosenRecyclerView extends RecyclerView.Adapter<AdapterDosenRecyclerView.ViewHolder> {
    private ArrayList<Dosen> daftarDosen;
    private Context context;

    public AdapterDosenRecyclerView(ArrayList<Dosen> dosens, Context ctx) {
        // Inisiasi data dan variabel yang akan digunakan
        daftarDosen = dosens;
        context = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Inisiasi View
        TextView tvTitle;

        ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_namadosen);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inisiasi ViewHolder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dosen, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Menampilkan data pada view
        final String name = daftarDosen.get(position).getNik();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read detail data
            }
        });

        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Delete dan update data
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = dialog.findViewById(R.id.bt_edit_data);
                Button delButton = dialog.findViewById(R.id.bt_delete_data);

                // aksi tombol edit di klik
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        context.startActivity(DBCreateActivity.getActIntent((Activity) context).putExtra("data", daftarDosen.get(position)));
                    }
                });

                // aksi tombol delete di klik
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Delete data
                        hapusDosen(position);
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

        holder.tvTitle.setText(name);
    }

    // Fungsi untuk menghapus dosen
    private void hapusDosen(final int position) {
        // Dapatkan referensi Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // Dapatkan key dari item yang akan dihapus
        String key = daftarDosen.get(position).getKey();

        // Menghapus data dari Firebase
        ref.child("dosen").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Data berhasil dihapus
                Snackbar.make(((Activity) context).findViewById(R.id.rv_main), "Data dosen berhasil dihapus", Snackbar.LENGTH_LONG)
                        .setAction("OKE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Aksi ketika snackbar di-tap, bisa diisi dengan logika yang Anda inginkan
                            }
                        }).show();

                // Menghapus data dari ArrayList
                daftarDosen.remove(position);
                // Memberitahu adapter tentang item yang dihapus
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, daftarDosen.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Data gagal dihapus
                Snackbar.make(((Activity) context).findViewById(R.id.rv_main), "Gagal menghapus data dosen", Snackbar.LENGTH_LONG)
                        .setAction("COBA LAGI", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Aksi ketika snackbar di-tap, bisa diisi dengan logika untuk mencoba menghapus lagi
                            }
                        }).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        // Mengembalikan jumlah item
        return daftarDosen.size();
    }
}
