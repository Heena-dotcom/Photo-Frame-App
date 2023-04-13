package crephotoseditor.valentinephotoeditor.act;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.Glob.ImageUtil;
import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.TouchListner.MultiTouchListener;
import crephotoseditor.valentinephotoeditor.Utils.Effects;
import crephotoseditor.valentinephotoeditor.adapter.CardFontStyleAdapter;
import crephotoseditor.valentinephotoeditor.adapter.EffectAdapter;
import crephotoseditor.valentinephotoeditor.adapter.FrameAdapter;
import crephotoseditor.valentinephotoeditor.adapter.OverlayAdapter;
import crephotoseditor.valentinephotoeditor.adapter.StickerAdapter;
import crephotoseditor.valentinephotoeditor.clr.ColorPickerDialog;
import crephotoseditor.valentinephotoeditor.clr.ColorPickerDialogListener;
import crephotoseditor.valentinephotoeditor.customview.MySquareFrame;
import crephotoseditor.valentinephotoeditor.fab.FloatingActionButton;
import crephotoseditor.valentinephotoeditor.fab.FloatingActionMenu;
import crephotoseditor.valentinephotoeditor.model.FrameListModel;
import crephotoseditor.valentinephotoeditor.stickerview.RemoveOutline;
import crephotoseditor.valentinephotoeditor.stickerview.StickerTextView;
import crephotoseditor.valentinephotoeditor.stickerview.StickerView;

import static crephotoseditor.valentinephotoeditor.Glob.ImageUtil.CropBitmapTransparency;
import static crephotoseditor.valentinephotoeditor.Glob.ImageUtil.loadBitmapFromView;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener, FrameAdapter.clickFrame, ColorPickerDialogListener {

    private static final int REQ_READ_STORAGE = 100;
    private static final int RESULT_GALLERY = 1;
    MySquareFrame mainFlout;
    ImageView ivOriginalImage, ivFrame, ivOverlay, ivSave, ivSelectImage, ivSelectFrame, ivSelectOverlay, ivSelectFilter, ivSelectText, ivSelectSticker;
    private int dw;
    private int dh;
    Bitmap bitmap;
    int frameId;
    private View view;
    Uri imageUri;
    RecyclerView rvEffectlist, rvStickerList, rvOverlayList;
    private ArrayList<FrameListModel> effectList, stickerList, overlatList;
    EffectAdapter effecctAdapter;
    EffectInterface effectInterface;
    private StickerAdapter stickerAdapter;
    private FrameLayout floutStickerText;
    private OverlayAdapter overlayAdapter;
    boolean isImageSet = false;
    private Bitmap editedBitmap;
    public static String imagePath;
    //declaration for new text*********************************
    InputMethodManager imm;
    private EditText edittext;
    private ImageView iv_done;
    String[] fonts = new String[]{"font22.ttf", "font23.ttf", "font24.ttf", "font26.ttf", "font28.TTF", "font25.ttf", "font30.ttf", "font31.otf", "font35.TTF", "font2.ttf", "font38.ttf", "font36.TTF", "font9.ttf", "font37.OTF", "font5.ttf", "font3.ttf", "font4.TTF", "font6.TTF", "font11.ttf", "font12.ttf", "font16.TTF", "font17.ttf"};
    private Typeface type;
    private LinearLayout lyfontlist;
    private ImageView iv_color;
    private LinearLayout lycolorlist;
    private GridView gvcolorlist;
    private GridView gvfontlist;
    private static int columnWidth = 80;
    private int mPickedColor = Color.WHITE;
    private ImageView iv_keyboard;
    private ImageView iv_fontstyle;
    private ImageView iv_gravity;
    private int w = 0;
    private ArrayList<View> mStickers = new ArrayList<View>();
    private StickerTextView mCurrentTextView;
    private StickerView mCurrentView;
    //declaration for new text****************************

    SeekBar seekContrast, seekOpacity, seekOverlayOpacity;
    LinearLayout loutOpacity, loutContrast, loutOverlayOpacity;
    ImageView ivOpacity, ivContast;
    private Bitmap cBitmap;
    int brightnessValue = 0;

    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;
    private AdView mAdView;
    public ArrayList<FrameListModel> frameList;
    Dialog dialog = null;
    private FloatingActionMenu text_menu;
    private FloatingActionButton fabFont, fabColor;
    private TextView textView;
    private AdView mAdView1;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        frameId = getIntent().getIntExtra("frame", 1);

        if (Glob.abanner != null && !Glob.abanner.equals("")) {
            showAdmobBanner();
        }

        if (Glob.ainterstitial1 != null && !Glob.ainterstitial1.equals("")) {
            mInterstitialAdMob = showAdmobFullAd();
            loadAdmobAd();
        }

        if (Glob.finterstitial1 != null && !Glob.finterstitial1.equals("")) {
            loadFBInterstitial();
        }

        Display display = getWindowManager().getDefaultDisplay();
        dw = display.getWidth();
        dh = display.getHeight();

        bindView();


        seekContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessValue = progress - 255;
                cBitmap = contrastAndBrightnessControler(bitmap, seekContrast.getProgress() / 10, seekContrast.getProgress() / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                ivOriginalImage.setImageBitmap(cBitmap);

            }
        });

        seekOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivFrame.setImageAlpha((seekOpacity.getProgress())); //for API Level 16+
                } else
                    ivFrame.setAlpha((seekOpacity.getProgress()));   //deprecated
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekOverlayOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivOverlay.setImageAlpha((seekOverlayOpacity.getProgress())); //for API Level 16+
                } else
                    ivOverlay.setAlpha((seekOverlayOpacity.getProgress()));   //deprecated

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        effectInterface = new EffectInterface() {

            @Override
            public void applyEffect(int filterPosition) {
                switch (filterPosition) {
                    case 0:
                        Effects.applyEffectNone(ivOriginalImage);
                        break;
                    case 1:
                        Effects.applyEffect1(ivOriginalImage);
                        break;
                    case 2:
                        Effects.applyEffect2(ivOriginalImage);
                        break;
                    case 3:
                        Effects.applyEffect4(ivOriginalImage);
                        break;
                    case 4:
                        Effects.applyEffect5(ivOriginalImage);
                        break;
                    case 5:
                        Effects.applyEffect6(ivOriginalImage);
                        break;
                    case 6:
                        Effects.applyEffect7(ivOriginalImage);
                        break;
                    case 7:
                        Effects.applyEffect9(ivOriginalImage);
                        break;
                    case 8:
                        Effects.applyEffect12(ivOriginalImage);
                        break;
                    case 9:
                        Effects.applyEffect14(ivOriginalImage);
                        break;
                    case 10:
                        Effects.applyEffect15(ivOriginalImage);
                        break;
                    case 11:
                        Effects.applyEffect16(ivOriginalImage);
                        break;
                    case 12:
                        Effects.applyEffect17(ivOriginalImage);
                        break;
                    case 13:
                        Effects.applyEffect18(ivOriginalImage);
                        break;
                    case 14:
                        Effects.applyEffect19(ivOriginalImage);
                        break;
                    case 15:
                        Effects.applyEffect20(ivOriginalImage);
                        break;
                    case 16:
                        Effects.applyEffect21(ivOriginalImage);
                        break;
                    case 17:
                        Effects.applyEffect22(ivOriginalImage);
                        break;

                    default:
                        break;

                }
            }

            @Override
            public void addSticker(int stickerPosition) {
                final StickerView stickerView = new StickerView(EditImageActivity.this);
                stickerView.setImageResource(stickerList.get(stickerPosition).getEffect_thumb());
                stickerView.setOperationListener(new StickerView.OperationListener() {
                    @Override
                    public void onDeleteClick() {
                        mStickers.remove(stickerView);
                        floutStickerText.removeView(stickerView);
                    }

                    @Override
                    public void onEdit(StickerView stickerView) {
                        mCurrentView.setInEdit(false);
                        mCurrentView = stickerView;
                        mCurrentView.setInEdit(true);
                    }

                    @Override
                    public void onTop(StickerView stickerView) {
                        int position = mStickers.indexOf(stickerView);
                        if (position == mStickers.size() - 1) {
                            return;
                        }
                        StickerView stickerTemp = (StickerView) mStickers.remove(position);
                        mStickers.add(mStickers.size(), stickerTemp);
                    }
                });
                floutStickerText.addView(stickerView);
                mStickers.add(stickerView);
                setCurrentEdit(stickerView);
            }

            @Override
            public void applyOverlay(int overlayPosition) {

                if (overlayPosition > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ivOverlay.setImageAlpha((seekOverlayOpacity.getProgress())); //for API Level 16+
                    } else
                        ivOverlay.setAlpha((seekOverlayOpacity.getProgress()));   //deprecated

                    ivOverlay.setImageResource(overlatList.get(overlayPosition).getFrame());
                } else {
                    ivOverlay.setImageResource(0);

                }
            }
        };

    }

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            int value = (int) (progress - 100) * 255 / 100;

            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);

        } else {
            int value = (int) (100 - progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);


        }
    }

    private void bindView() {
        mStickers = new ArrayList<>();
        mainFlout = (MySquareFrame) findViewById(R.id.mainFramelout);
        ivOriginalImage = (ImageView) findViewById(R.id.ivOriginalImage);
        Intent intent = getIntent();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(new File(intent.getData().getPath()).getAbsolutePath(), options);

        new a(this, true).execute(new String[]{intent.getData().getPath()});


        ivFrame = (ImageView) findViewById(R.id.ivFrame);
        ivOverlay = (ImageView) findViewById(R.id.ivOverlay);

        ivSave = (ImageView) findViewById(R.id.ivSave);

        ivSelectImage = (ImageView) findViewById(R.id.ivSelectImage);
        ivSelectFrame = (ImageView) findViewById(R.id.ivSelectFrame);
        ivSelectOverlay = (ImageView) findViewById(R.id.ivSelectOverlay);
        ivSelectFilter = (ImageView) findViewById(R.id.ivSelectFilter);
        ivSelectText = (ImageView) findViewById(R.id.ivSelectText);
        ivSelectSticker = (ImageView) findViewById(R.id.ivSelectSticker);
        loutContrast = (LinearLayout) findViewById(R.id.loutContrast);
        loutOpacity = (LinearLayout) findViewById(R.id.loutOpacity);
        loutOverlayOpacity = (LinearLayout) findViewById(R.id.loutOverlayOpacity);

        floutStickerText = (FrameLayout) findViewById(R.id.floutStickerText);
        floutStickerText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeOutline.removeBorder();
                return false;
            }
        });

        ivOpacity = (ImageView) findViewById(R.id.ivOpacity);
        ivContast = (ImageView) findViewById(R.id.ivContast);
        rvEffectlist = (RecyclerView) findViewById(R.id.rvEffectList);
        seekContrast = (SeekBar) findViewById(R.id.seekContrast);
        seekOpacity = (SeekBar) findViewById(R.id.seekOpacity);
        seekOverlayOpacity = (SeekBar) findViewById(R.id.seekOverlayOpacity);
        rvEffectlist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(EditImageActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        rvEffectlist.setLayoutManager(layoutManager);

        rvStickerList = (RecyclerView) findViewById(R.id.rvStickerList);
        rvStickerList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(EditImageActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        rvStickerList.setLayoutManager(layoutManager1);

        rvOverlayList = (RecyclerView) findViewById(R.id.rvOverlayList);
        rvOverlayList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(EditImageActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        rvOverlayList.setLayoutManager(layoutManager2);

        ivSave.setOnClickListener(this);
        ivOpacity.setOnClickListener(this);
        ivContast.setOnClickListener(this);
        ivSelectImage.setOnClickListener(this);
        ivSelectFrame.setOnClickListener(this);
        ivSelectOverlay.setOnClickListener(this);
        ivSelectFilter.setOnClickListener(this);
        ivSelectText.setOnClickListener(this);
        ivSelectSticker.setOnClickListener(this);

        ivOriginalImage.setOnTouchListener(new MultiTouchListener());
        ivFrame.setImageDrawable(getResources().getDrawable(frameId));

        ivFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                removeBorder();
                setViewVisibility(rvEffectlist, "gone");
                return false;
            }
        });

    }

    void removeBorder() {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentTextView != null) {
            mCurrentTextView.setInEdit(false);
        }
    }


    public static Bitmap contrastAndBrightnessControler(Bitmap bitmap, float contrast, float brightness) {

        ColorMatrix cmatrix = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        Bitmap ret = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();

        paint.setColorFilter(new ColorMatrixColorFilter(cmatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return ret;
    }


    RemoveOutline removeOutline = new RemoveOutline() {
        @Override
        public void removeBorder() {
            if (mCurrentView != null) {
                mCurrentView.setInEdit(false);
            }
            if (mCurrentTextView != null) {
                mCurrentTextView.setInEdit(false);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        view = v;
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                break;
            case R.id.ivSave:
                if (isImageSet) {
                    removeBorder();
                    setViewVisibility(rvEffectlist, "gone");
                    editedBitmap = getBitmapFromView(mainFlout);
                    saveImage(editedBitmap);
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.enter_transition, 0);
                } else {
                    Snackbar.make(view, "Please select image", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivSelectImage:
                setViewVisibility(rvEffectlist, "gone");
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    openGallery();
                }
                break;

            case R.id.ivSelectFrame:
                selectFrame();
                break;

            case R.id.ivSelectFilter:
                setArraylistForFilter();
                if (isImageSet) {
                    setViewVisibility(rvEffectlist, "filter");
                    effecctAdapter = new EffectAdapter(EditImageActivity.this, effectList, effectInterface);
                    rvEffectlist.setAdapter(effecctAdapter);
                } else {
                    Snackbar.make(view, "Please select image", Snackbar.LENGTH_SHORT).show();

                }
                break;

            case R.id.ivSelectSticker:
                if (isImageSet) {
                    setListForSticker();
                    setViewVisibility(rvStickerList, "sticker");
                    stickerAdapter = new StickerAdapter(EditImageActivity.this, stickerList, effectInterface);
                    rvStickerList.setAdapter(stickerAdapter);
                } else {
                    Snackbar.make(view, "Please select image", Snackbar.LENGTH_SHORT).show();

                }
                break;

            case R.id.ivSelectOverlay:
                if (isImageSet) {
                    setListForOverlay();
                    setViewVisibility(loutOverlayOpacity, "overlay");
                    overlayAdapter = new OverlayAdapter(EditImageActivity.this, overlatList, effectInterface);
                    rvOverlayList.setAdapter(overlayAdapter);
                } else {
                    Snackbar.make(view, "Please select image", Snackbar.LENGTH_SHORT).show();

                }
                break;
            case R.id.ivSelectText:
                selecttext();
                break;
            case R.id.ivContast:
                if (isImageSet) {
                    setViewVisibility(loutContrast, "contrast");
                } else {
                    Snackbar.make(view, "Please select image", Snackbar.LENGTH_SHORT).show();

                }
                break;
            case R.id.ivOpacity:
                if (isImageSet) {
                    setViewVisibility(loutOpacity, "opacity");
                } else {
                    Snackbar.make(view, "Please select image", Snackbar.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void selectFrame() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_frame);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();
        showFBInterstitial();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window2 = dialog.getWindow();
        layoutParams.copyFrom(window2.getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -1;
        window2.setAttributes(layoutParams);

        frameList = ImageUtil.setListForFrame();
        RecyclerView rvFrameList = (RecyclerView) dialog.findViewById(R.id.rvFrameList);
        ImageView back = (ImageView) dialog.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rvFrameList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(EditImageActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rvFrameList.setLayoutManager(layoutManager);

        FrameAdapter frameAdapter = new FrameAdapter(EditImageActivity.this, frameList, this);
        rvFrameList.setAdapter(frameAdapter);

    }

    protected void selecttext() {
        final Dialog dialog = new Dialog(EditImageActivity.this);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.text_layout);
        mPickedColor = Color.WHITE;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        textView = new TextView(this);
        edittext = (EditText) dialog.findViewById(R.id.edittext);
        edittext.requestFocus();


        text_menu = (FloatingActionMenu) dialog.findViewById(R.id.text_menu);
        fabColor = (FloatingActionButton) dialog.findViewById(R.id.fabColor);
        fabFont = (FloatingActionButton) dialog.findViewById(R.id.fabFont);

        fabColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) EditImageActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext.getWindowToken(), 0);

                ColorPickerDialog.newBuilder()
                        .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                        .setAllowPresets(false)
                        .setDialogId(0)
                        .setShowAlphaSlider(true)
                        .setColor(mPickedColor)
                        .show(EditImageActivity.this);
                text_menu.close(true);
            }
        });

        fabFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_menu.close(true);
                ((InputMethodManager) EditImageActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext.getWindowToken(), 0);

                final Dialog fontDialog = new Dialog(EditImageActivity.this);
                Window window = fontDialog.getWindow();
                fontDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                fontDialog.setContentView(R.layout.text_layout2);

                GridView fontGrid = (GridView) fontDialog.findViewById(R.id.fontGrid);
                fontGrid.setAdapter(new CardFontStyleAdapter(EditImageActivity.this, fonts));
                fontGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        type = Typeface.createFromAsset(getAssets(), fonts[i]);
                        edittext.setTypeface(type);
                        textView.setTypeface(type);
                        fontDialog.dismiss();
                    }
                });
                fontDialog.show();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                Window window2 = fontDialog.getWindow();
                layoutParams.copyFrom(window2.getAttributes());
                layoutParams.width = -1;
                layoutParams.height = -1;
                window2.setAttributes(layoutParams);

            }
        });

        lyfontlist = (LinearLayout) dialog.findViewById(R.id.lyfontlist);
        lyfontlist.setVisibility(View.GONE);


        lycolorlist = (LinearLayout) dialog.findViewById(R.id.lycolorlist);
        lycolorlist.setVisibility(View.GONE);
        final ArrayList colors = HSVColors();
        ArrayAdapter<Integer> ad = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_list_item_1, colors) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                int currentColor = (int) colors.get(position);
                view.setBackgroundColor(currentColor);
                view.setText("");
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT
                );
                view.setLayoutParams(lp);
                AbsListView.LayoutParams params = (AbsListView.LayoutParams) view.getLayoutParams();
                params.width = columnWidth; // pixels
                params.height = columnWidth; // pixels
                view.setLayoutParams(params);
                view.requestLayout();
                return view;
            }
        };

        iv_keyboard = (ImageView) dialog.findViewById(R.id.iv_keyboard);
        iv_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) EditImageActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edittext, InputMethodManager.SHOW_FORCED);
                lyfontlist.setVisibility(View.GONE);
                lycolorlist.setVisibility(View.GONE);

            }
        });


        iv_gravity = (ImageView) dialog.findViewById(R.id.iv_gravity);

        iv_done = (ImageView) dialog.findViewById(R.id.iv_done);
        final TextView txtEnteredText = (TextView) dialog.findViewById(R.id.txtEnteredText);
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = edittext.getText().toString();
                if (st.matches("")) {
                    Toast.makeText(EditImageActivity.this, "Please enter text First", Toast.LENGTH_SHORT).show();
                } else {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    st = edittext.getText().toString();
                    if (!st.isEmpty()) {
                        txtEnteredText.setText(st);
                        txtEnteredText.setTypeface(edittext.getTypeface());
                        txtEnteredText.setTextColor(edittext.getTextColors());
                        txtEnteredText.setGravity(edittext.getGravity());

                        ImageView textImg = new ImageView(EditImageActivity.this);
                        Bitmap bt = getLayoutBitmap(txtEnteredText);
                        textImg.setImageBitmap(bt);
                        ImageUtil.textBitmap = loadBitmapFromView(textImg);
                        ImageUtil.textBitmap = CropBitmapTransparency(ImageUtil.textBitmap);

                        ((InputMethodManager) EditImageActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                    } else {
                        Toast.makeText(EditImageActivity.this, "text empty", Toast.LENGTH_SHORT).show();
                    }
                    final StickerTextView stickerTextView = new StickerTextView(EditImageActivity.this);
                    stickerTextView.setBitmap(ImageUtil.textBitmap);
                    FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                    floutStickerText.addView(stickerTextView, lp1);
                    mStickers.add(stickerTextView);
                    setCurrentEditForText(stickerTextView);
                    stickerTextView.setOperationListener(new StickerTextView.OperationListener() {
                        @Override
                        public void onDeleteClick() {
                            mStickers.remove(stickerTextView);
                            floutStickerText.removeView(stickerTextView);
                        }

                        @Override
                        public void onEdit(StickerTextView customTextView) {
                            mCurrentTextView.setInEdit(false);
                            mCurrentTextView = customTextView;
                            mCurrentTextView.setInEdit(true);
//                            if (mCurrentView != null) {
//                                mCurrentView.setInEdit(false);
//                            }
                        }

                        @Override
                        public void onTop(StickerTextView customTextView) {
                            int position = mStickers.indexOf(customTextView);
                            if (position == mStickers.size() - 1) {
                                return;
                            }
                            StickerTextView stickerTemp = (StickerTextView) mStickers.remove(position);
                            mStickers.add(mStickers.size(), stickerTemp);
                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window2 = dialog.getWindow();
        layoutParams.copyFrom(window2.getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -1;
        window2.setAttributes(layoutParams);

    }

    private void setCurrentEditForText(StickerTextView customTextView) {
        if (mCurrentTextView != null) {
            mCurrentTextView.setInEdit(false);
        }
        mCurrentTextView = customTextView;
        customTextView.setInEdit(true);
    }

    private Bitmap getLayoutBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static ArrayList HSVColors() {
        ArrayList<Integer> colors = new ArrayList<>();

        // Loop through hue channel, saturation and light full
        for (int h = 0; h <= 360; h += 20) {
            colors.add(HSVColor(h, 1, 1));
        }

        // Loop through hue channel, different saturation and light full
        for (int h = 0; h <= 360; h += 20) {
            colors.add(HSVColor(h, .25f, 1));
            colors.add(HSVColor(h, .5f, 1));
            colors.add(HSVColor(h, .75f, 1));
        }

        // Loop through hue channel, saturation full and light different
        for (int h = 0; h <= 360; h += 20) {
            //colors.add(createColor(h, 1, .25f));
            colors.add(HSVColor(h, 1, .5f));
            colors.add(HSVColor(h, 1, .75f));
        }

        // Loop through the light channel, no hue no saturation
        // It will generate gray colors
        for (float b = 0; b <= 1; b += .10f) {
            colors.add(HSVColor(0, 0, b));
        }
        return colors;
    }

    public static int HSVColor(float hue, float saturation, float black) {
        /*
            Hue is the variation of color
            Hue range 0 to 360

            Saturation is the depth of color
            Range is 0.0 to 1.0 float value
            1.0 is 100% solid color

            Value/Black is the lightness of color
            Range is 0.0 to 1.0 float value
            1.0 is 100% bright less of a color that means black
        */
        int color = Color.HSVToColor(255, new float[]{hue, saturation, black});
        return color;
    }
    //new text dialog over
    //=====================================================================

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    private void saveImage(Bitmap editedBitmap) {

        File dir = new File(ImageUtil.RootPath + "/" + getResources().getString(R.string.app_name));
        if (!dir.exists())
            dir.mkdirs();

        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String FileName = ts + ".jpg";
        File file = new File(dir, FileName);

        imagePath = ImageUtil.RootPath + "/" + getResources().getString(R.string.app_name) + "/" + FileName;
//        MediaScannerConnection.scanFile(EditImageActivity.this, new String[]{imagePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
//            public void onScanCompleted(String path, Uri uri) {
//            }
//        });
        try {
            OutputStream output = new FileOutputStream(file);
            editedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }


    void setViewVisibility(View view, String type) {
        if (type.equals("filter")) {
            ((RecyclerView) view).setVisibility(((RecyclerView) view).getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            rvStickerList.setVisibility(View.GONE);
            loutOpacity.setVisibility(View.GONE);
            loutOverlayOpacity.setVisibility(View.GONE);
            loutContrast.setVisibility(View.GONE);
        } else if (type.equals("sticker")) {
            ((RecyclerView) view).setVisibility(((RecyclerView) view).getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            rvEffectlist.setVisibility(View.GONE);
            loutOverlayOpacity.setVisibility(View.GONE);
            loutOpacity.setVisibility(View.GONE);
            loutContrast.setVisibility(View.GONE);
        } else if (type.equals("overlay")) {
            ((LinearLayout) view).setVisibility(((LinearLayout) view).getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            rvEffectlist.setVisibility(View.GONE);
            rvStickerList.setVisibility(View.GONE);

            loutOpacity.setVisibility(View.GONE);
            loutContrast.setVisibility(View.GONE);
        } else if (type.equals("contrast")) {
            (view).setVisibility((view).getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            rvEffectlist.setVisibility(View.GONE);
            rvStickerList.setVisibility(View.GONE);
            loutOverlayOpacity.setVisibility(View.GONE);
            loutOpacity.setVisibility(View.GONE);
        } else if (type.equals("opacity")) {
            (view).setVisibility((view).getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            rvEffectlist.setVisibility(View.GONE);
            rvStickerList.setVisibility(View.GONE);
            loutOverlayOpacity.setVisibility(View.GONE);
            loutContrast.setVisibility(View.GONE);
        } else if (type.equals("gone")) {
            rvEffectlist.setVisibility(View.GONE);
            rvStickerList.setVisibility(View.GONE);
            loutOverlayOpacity.setVisibility(View.GONE);
            loutOpacity.setVisibility(View.GONE);
            loutContrast.setVisibility(View.GONE);
        }

    }

    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_GALLERY);
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_GALLERY:
                    imageUri = data.getData();
                    try {
                        isImageSet = true;
                        new a(this, true).execute(new String[]{imageUri.toString()});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    class a extends AsyncTask<String, Void, Bitmap> {

        final EditImageActivity b;
        public a(EditImageActivity photoEditActivity, boolean z) {
            this.b = photoEditActivity;

        }
        public Bitmap a(String str) {
            int i;
            Bitmap createBitmap;
            String b = b(str);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap decodeFile = BitmapFactory.decodeFile(b, options);
            int i2 = options.outHeight;
            int i3 = options.outWidth;
            float f = (float) (i3 / i2);
            if (((float) i2) <= 2048.0f && ((float) i3) <= 1440.0f) {
                i = i2;
                i2 = i3;
            } else if (f < 0.703125f) {
                i = 2048;
                i2 = (int) (((float) i3) * (2048.0f / ((float) i2)));
            } else if (f > 0.703125f) {
                i = (int) ((1440.0f / ((float) i3)) * ((float) i2));
                i2 = 1440;
            } else {
                i = 2048;
                i2 = 1440;
            }
            options.inSampleSize = bvva(options, i2, i);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16384];
            try {
                decodeFile = BitmapFactory.decodeFile(b, options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            try {
                createBitmap = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
                createBitmap = null;
            }
            float f2 = ((float) i2) / ((float) options.outWidth);
            float f3 = ((float) i) / ((float) options.outHeight);
            float f4 = ((float) i2) / 2.0f;
            f = ((float) i) / 2.0f;
            Matrix matrix = new Matrix();
            matrix.setScale(f2, f3, f4, f);
            Canvas canvas = new Canvas(createBitmap);
            canvas.setMatrix(matrix);
            canvas.drawBitmap(decodeFile, f4 - ((float) (decodeFile.getWidth() / 2)), f - ((float) (decodeFile.getHeight() / 2)), new Paint(2));
            try {
                i2 = new ExifInterface(b).getAttributeInt("Orientation", 0);
                Matrix matrix2 = new Matrix();
                if (i2 == 6) {
                    matrix2.postRotate(90.0f);
                } else if (i2 == 3) {
                    matrix2.postRotate(180.0f);
                } else if (i2 == 8) {
                    matrix2.postRotate(270.0f);
                }
                createBitmap = Bitmap.createBitmap(createBitmap, 0, 0, createBitmap.getWidth(), createBitmap.getHeight(), matrix2, true);
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return createBitmap;
        }

        private String b(String str) {
            Uri parse = Uri.parse(str);
            Cursor query = this.b.getContentResolver().query(parse, null, null, null, null);
            if (query == null) {
                return parse.getPath();
            }
            query.moveToFirst();
            return query.getString(query.getColumnIndex("_data"));
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            return a(params[0]);
        }


        @Override
        protected void onPostExecute(Bitmap bt) {
            super.onPostExecute(bt);
            ivOriginalImage.setImageBitmap(bt);
            isImageSet = true;
            bitmap = bt;
            showAdmobInterstitial();
        }
    }


    public static int bvva(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            i5 = Math.round(((float) i3) / ((float) i2));
            int round = Math.round(((float) i4) / ((float) i));
            if (i5 >= round) {
                i5 = round;
            }
        }
        while (((float) (i4 * i3)) / ((float) (i5 * i5)) > ((float) ((i * i2) * 2))) {
            i5++;
        }
        return i5;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_STORAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_READ_STORAGE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        openGallery();
                        Snackbar.make(view, "Permission Granted, Now you can access external storage.", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(view, "Permission Denied, You cannot access external storage.", Snackbar.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_STORAGE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EditImageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void setArraylistForFilter() {
        effectList = new ArrayList<FrameListModel>();
        for (int i = 0; i < 18; i++)
            effectList.add(new FrameListModel(R.drawable.thumb));

    }


    private void setListForSticker() {
        stickerList = new ArrayList<FrameListModel>();

        stickerList.add(new FrameListModel(R.mipmap.s1));
        stickerList.add(new FrameListModel(R.mipmap.s2));
        stickerList.add(new FrameListModel(R.mipmap.s3));
        stickerList.add(new FrameListModel(R.mipmap.s4));
        stickerList.add(new FrameListModel(R.mipmap.s5));
        stickerList.add(new FrameListModel(R.mipmap.s6));
        stickerList.add(new FrameListModel(R.mipmap.s7));
        stickerList.add(new FrameListModel(R.mipmap.s8));
        stickerList.add(new FrameListModel(R.mipmap.s9));
        stickerList.add(new FrameListModel(R.mipmap.s10));
        stickerList.add(new FrameListModel(R.mipmap.s11));
        stickerList.add(new FrameListModel(R.mipmap.s12));
        stickerList.add(new FrameListModel(R.mipmap.s13));
        stickerList.add(new FrameListModel(R.mipmap.s14));
        stickerList.add(new FrameListModel(R.mipmap.s15));
        stickerList.add(new FrameListModel(R.mipmap.s16));
        stickerList.add(new FrameListModel(R.mipmap.s17));
        stickerList.add(new FrameListModel(R.mipmap.s18));
        stickerList.add(new FrameListModel(R.mipmap.s19));
        stickerList.add(new FrameListModel(R.mipmap.s20));
        stickerList.add(new FrameListModel(R.mipmap.s21));
        stickerList.add(new FrameListModel(R.mipmap.s22));
    }

    private void setListForOverlay() {
        overlatList = new ArrayList<FrameListModel>();
        overlatList.add(new FrameListModel(R.mipmap.ic_none, R.mipmap.ic_none));
        overlatList.add(new FrameListModel(R.mipmap.boke19, R.mipmap.boke19));
        overlatList.add(new FrameListModel(R.mipmap.boke20, R.mipmap.boke20));
        overlatList.add(new FrameListModel(R.mipmap.boke15, R.mipmap.boke15));
        overlatList.add(new FrameListModel(R.mipmap.boke16, R.mipmap.boke16));
        overlatList.add(new FrameListModel(R.mipmap.boke3, R.mipmap.boke3));
        overlatList.add(new FrameListModel(R.mipmap.boke4, R.mipmap.boke4));
        overlatList.add(new FrameListModel(R.mipmap.boke5, R.mipmap.boke5));
        overlatList.add(new FrameListModel(R.mipmap.boke21, R.mipmap.boke21));
        overlatList.add(new FrameListModel(R.mipmap.boke22, R.mipmap.boke22));
        overlatList.add(new FrameListModel(R.mipmap.boke23, R.mipmap.boke23));
        overlatList.add(new FrameListModel(R.mipmap.boke24, R.mipmap.boke24));
        overlatList.add(new FrameListModel(R.mipmap.boke25, R.mipmap.boke25));
        overlatList.add(new FrameListModel(R.mipmap.boke1, R.mipmap.boke1));
        overlatList.add(new FrameListModel(R.mipmap.boke2, R.mipmap.boke2));
        overlatList.add(new FrameListModel(R.mipmap.boke11, R.mipmap.boke11));
        overlatList.add(new FrameListModel(R.mipmap.boke12, R.mipmap.boke12));
        overlatList.add(new FrameListModel(R.mipmap.boke6, R.mipmap.boke6));
        overlatList.add(new FrameListModel(R.mipmap.boke7, R.mipmap.boke7));
        overlatList.add(new FrameListModel(R.mipmap.boke8, R.mipmap.boke8));
        overlatList.add(new FrameListModel(R.mipmap.boke14, R.mipmap.boke14));
        overlatList.add(new FrameListModel(R.mipmap.boke17, R.mipmap.boke17));
        overlatList.add(new FrameListModel(R.mipmap.boke18, R.mipmap.boke18));
        overlatList.add(new FrameListModel(R.mipmap.boke9, R.mipmap.boke9));
        overlatList.add(new FrameListModel(R.mipmap.boke10, R.mipmap.boke10));
        overlatList.add(new FrameListModel(R.mipmap.boke13, R.mipmap.boke13));


    }

    @Override
    public void clickedFrame(int pos) {
        ivFrame.setImageDrawable(getResources().getDrawable(pos));
        dialog.dismiss();
    }

    @Override
    public void onColorSelected(int dialogId, @ColorInt int color) {
        mPickedColor = color;
        edittext.setTextColor(mPickedColor);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        textView.setTextColor(mPickedColor);

    }

    public interface EffectInterface {
        void applyEffect(int filterPosition);

        void addSticker(int stickerPosition);

        void applyOverlay(int overlayPosition);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(EditImageActivity.this);
        builder.setTitle("Alert")
                .setMessage("Changes in photo will be deleted. Are you want to keep Editing?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        overridePendingTransition(R.anim.leave_transition, 0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        finish();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //AdMob InterstitialAds start
    private com.google.android.gms.ads.InterstitialAd showAdmobFullAd() {
        com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(Glob.ainterstitial1);
        interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                loadAdmobAd();
            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdOpened() {
                //   super.onAdOpened();
            }
        });
        return interstitialAd;
    }

    private void loadAdmobAd() {
        this.mInterstitialAdMob.loadAd(new AdRequest.Builder().build());
    }

    private void showAdmobInterstitial() {
        if (this.mInterstitialAdMob != null && this.mInterstitialAdMob.isLoaded()) {
            this.mInterstitialAdMob.show();
        }
    }
    //AdMob InterstitialAds End

    public boolean isNetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting())
                    || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

    private void showAdmobBanner() {
        mAdView = new AdView(EditImageActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(Glob.abanner);
        final RelativeLayout layout = findViewById(R.id.loutadMobView);
        layout.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                layout.setVisibility(View.GONE);
            }
        });
    }

    public void loadFBInterstitial() {
        interstitialAd = new InterstitialAd(getApplicationContext(), Glob.finterstitial1);
        interstitialAd.loadAd();

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadFBInterstitial();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });


    }

    public void showFBInterstitial() {
        if (interstitialAd != null && interstitialAd.isAdLoaded())
            interstitialAd.show();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
