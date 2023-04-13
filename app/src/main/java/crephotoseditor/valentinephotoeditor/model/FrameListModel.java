package crephotoseditor.valentinephotoeditor.model;

public class FrameListModel {

    int thumb;
    int frame;
    int effect_thumb;

    public FrameListModel(int effect_thumb) {
        this.effect_thumb = effect_thumb;
    }

    public FrameListModel(int thumb, int frame) {
        this.thumb = thumb;
        this.frame = frame;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getEffect_thumb() {
        return effect_thumb;
    }

    public void setEffect_thumb(int effect_thumb) {
        this.effect_thumb = effect_thumb;
    }


}
