package com.app.forms.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.ActivityCallback;
import com.app.forms.Items.BaseClass;
import com.app.forms.Items.Check;
import com.app.forms.Items.Response;
import com.app.forms.R;
import com.app.forms.activities.CreateFormActivity;
import com.app.forms.constants.Constants;
import com.app.forms.helpers.Utils;
import com.cooltechworks.views.WhatsAppTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import tyrantgit.explosionfield.ExplosionField;

public class FormPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<BaseClass> data;
    boolean preview;
    FirebaseStorage firebaseStorage;
    String formID;

    @Nullable
    ArrayList<Response> response;

    public FormPreviewAdapter(Context context, ArrayList<BaseClass> data, boolean preview, ArrayList<Response> response, String formID) {
        this.context = context;
        this.data = data;
        this.preview = preview;
        this.response = response;
        firebaseStorage = FirebaseStorage.getInstance();
        this.formID = formID;

    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case Constants.typeTextField:
                v = LayoutInflater.from(context).inflate(R.layout.show_text, parent, false);
                vh = new showTextHolder(v);
                break;
            case Constants.typepoll:
            case Constants.typeSingleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.show_single_check, parent, false);
                vh = new showSingleCheckHolder(v);
                break;
            case Constants.typeMultipleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.show_multiple_check, parent, false);
                vh = new showMultipleCheckHolder(v);
                break;
            case Constants.typeTextMsg:
                v = LayoutInflater.from(context).inflate(R.layout.show_msg, parent, false);
                vh = new showMsgHolder(v);
                break;
            case Constants.typerating:
                v = LayoutInflater.from(context).inflate(R.layout.show_rating, parent, false);
                vh = new showRatingHolder(v);
                break;
            case Constants.typeupload:
                v = LayoutInflater.from(context).inflate(R.layout.show_upload, parent, false);
                vh = new showUploadHolder(v);

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof showTextHolder) {

            ((showTextHolder) holder).title.setText(data.get(position).getTitle());

            if (true) { //show_count

                ((showTextHolder) holder).count.setVisibility(View.VISIBLE);
                ((showTextHolder) holder).count.setText("" + (position + 1) + ".");

            } else ((showTextHolder) holder).count.setVisibility(View.GONE);

        } else if (holder instanceof showSingleCheckHolder) {

            ((showSingleCheckHolder) holder).title.setText(data.get(position).getTitle());
            ArrayList<String> group = ((Check) data.get(position)).getGroup();
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int) context.getResources().getDimension(R.dimen.two_dp), 0, (int) context.getResources().getDimension(R.dimen.two_dp));

            for (String s : group) {

                RadioButton rb = prepareRadioButton(s);
                ((showSingleCheckHolder) holder).rg.addView(rb, lp);

            }
            if (true) { //show_count

                ((showSingleCheckHolder) holder).count.setVisibility(View.VISIBLE);
                ((showSingleCheckHolder) holder).count.setText("" + (position + 1) + ".");

            } else ((showTextHolder) holder).count.setVisibility(View.GONE);

        } else if (holder instanceof showMultipleCheckHolder) {

            ((showMultipleCheckHolder) holder).title.setText(data.get(position).getTitle());
            ArrayList<String> group = ((Check) data.get(position)).getGroup();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int) context.getResources().getDimension(R.dimen.two_dp), 0, (int) context.getResources().getDimension(R.dimen.two_dp));

            for (String s : group) {

                CheckBox v = prepareCheckBox(s, ((showMultipleCheckHolder) holder).checkedChangeListener);
                ((showMultipleCheckHolder) holder).lv.addView(v, lp);

            }

            if (true) { //show_count

                ((showMultipleCheckHolder) holder).count.setVisibility(View.VISIBLE);
                ((showMultipleCheckHolder) holder).count.setText("" + (position + 1) + ".");

            } else ((showTextHolder) holder).count.setVisibility(View.GONE);

        } else if (holder instanceof showMsgHolder) {

            ((showMsgHolder) holder).tv.setText(data.get(position).getTitle());

        } else if (holder instanceof showRatingHolder) {
            ((showRatingHolder) holder).title.setText(data.get(position).getTitle());
            if (true) {
                ((showRatingHolder) holder).count.setVisibility(View.VISIBLE);
                ((showRatingHolder) holder).count.setText("" + (position + 1));
            }
        } else if (holder instanceof showUploadHolder) {
            ((showUploadHolder) holder).title.setText(data.get(position).getTitle());
            if (true) {
                ((showUploadHolder) holder).count.setVisibility(View.VISIBLE);
                ((showUploadHolder) holder).count.setText("" + (position + 1));
            }

        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private RadioButton prepareRadioButton(String s) {
        RadioButton rb = new RadioButton(context);
        rb.setBackground(context.getDrawable(R.drawable.simple_border));
        rb.setTextSize(Dimension.DP, context.getResources().getDimension(R.dimen._18_dp));
        rb.setText(s);

        int pad = (int) context.getResources().getDimension(R.dimen.ten_dp);
        rb.setPadding(pad, pad, pad, pad);
        return rb;
    }

    private CheckBox prepareCheckBox(String s, @Nullable CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        CheckBox v = (CheckBox) LayoutInflater.from(context).inflate(R.layout.show_check, null);
        int pad = (int) context.getResources().getDimension(R.dimen.ten_dp);
        v.setPadding(pad, pad, pad, pad);
        v.setText(s);
        v.setOnCheckedChangeListener(checkedChangeListener);
        return v;


    }

    private class showTextHolder extends RecyclerView.ViewHolder {
        TextView count, title;
        TextInputEditText answer;

        public showTextHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            answer = itemView.findViewById(R.id.answer);

            if (!preview) {
                answer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int position = getAdapterPosition();
                        response.get(position).setText(s.toString());
                        if (s.toString().length() == 0) {
                            response.get(position).setMandatory(false);
                        } else response.get(position).setMandatory(true);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        }
    }

    private class showSingleCheckHolder extends RecyclerView.ViewHolder {
        TextView count, title;
        RadioGroup rg;

        public showSingleCheckHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            rg = itemView.findViewById(R.id.radiogroup);

            if (!preview) {
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int position = getAdapterPosition();
                        RadioButton rb = group.findViewById(checkedId);
                        response.get(position).setSchecked(group.indexOfChild(rb));
                        response.get(position).setMandatory(true);

                    }
                });
            }

        }
    }

    private class showMultipleCheckHolder extends RecyclerView.ViewHolder {
        TextView count, title;
        LinearLayout lv;
        CheckBox checkBox;
        CompoundButton.OnCheckedChangeListener checkedChangeListener = null;

        public showMultipleCheckHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            lv = itemView.findViewById(R.id.listview);

            if (!preview) {
                checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int position = getAdapterPosition();
                        int idx = lv.indexOfChild(buttonView);
                        if (isChecked) {
                            response.get(position).addIntoSet(idx);
                        } else {
                            response.get(position).getMchecked().remove(idx);
                        }
                        if (response.get(position).getMchecked().size() == 0) {
                            response.get(position).setMandatory(false);
                        } else response.get(position).setMandatory(true);
                    }
                };
            }

        }
    }

    private class showMsgHolder extends RecyclerView.ViewHolder {
        WhatsAppTextView tv;

        public showMsgHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    private class showRatingHolder extends RecyclerView.ViewHolder {
        TextView count, title, tv;
        SeekBar seekBar;
        ImageView iv;
        ExplosionField explosionField;
        int pxmin, px20;

        public showRatingHolder(@NonNull View itemView) {
            super(itemView);
            explosionField = ExplosionField.attach2Window((Activity) context);
            pxmin = (int) context.getResources().getDimension(R.dimen.iv_min);
            px20 = (int) context.getResources().getDimension(R.dimen._20_dp);
            seekBar = itemView.findViewById(R.id.seekbar);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    iv.getLayoutParams().height = pxmin;
                    iv.getLayoutParams().width = pxmin;
                    tv.setText("" + (progress + 1));
                    iv.setX(seekBar.getThumb().getBounds().centerX() + 2 * px20);
                    iv.requestLayout();
                    if (!preview) {
                        int position = getAdapterPosition();
                        response.get(position).setRating(progress + 1);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    explosionField.explode(iv);


                }
            });

        }
    }

    private class showUploadHolder extends RecyclerView.ViewHolder {
        TextView count, title, tv;
        LinearProgressIndicator progressIndicator;
        ActivityCallback activityCallback;

        public showUploadHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            tv = itemView.findViewById(R.id.tv);
            progressIndicator = itemView.findViewById(R.id.progressbar);
            activityCallback = new ActivityCallback() {
                @Override
                public void uploadFile(Uri uri) {
                    int position = getAdapterPosition();
                    response.get(position).setMandatory(false);
                    progressIndicator.setVisibility(View.VISIBLE);
                    String path = uri.getPath();
                    int idx = path.lastIndexOf(".");
                    String type = path.substring(idx);
                    String name = "" + Utils.generateUID(6) + type;

                    int _idx = path.lastIndexOf("/");
                    String filename = path.substring(_idx + 1);
                    tv.setText(filename);
                    if (Utils.getUserID() == null) {
                        View v = LayoutInflater.from(context).inflate(R.layout.login_required, null);
                        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(context);
                        alert.setView(v);
                        alert.show();
                        return;
                    }
                    InputStream inputStream = null;
                    StorageReference storageReference = firebaseStorage.getReference(formID + "/" + Utils.getUserID() + "/" + name);
                    try {

                        inputStream = context.getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    storageReference.putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressIndicator.setVisibility(View.GONE);
                            //TODO: update flag that file has been uploaded
                            int position = getAdapterPosition();
                            response.get(position).setMandatory(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressIndicator.setVisibility(View.GONE);
                            Toast.makeText(context, "Error while uploading file :(", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            };
            if (!preview) {
                tv.setOnClickListener(v -> {
                    ((CreateFormActivity) context).getFile(activityCallback);
                });
            }
        }
    }

}
//TODO add show count option in form settting, remove allow edit