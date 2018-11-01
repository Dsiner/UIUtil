package com.d.lib.common.view.toggle;

public interface ToggleView {

    /**
     * Toggle
     */
    void toggle();

    void setOpen(boolean open);

    boolean isOpen();

    void setOnToggleListener(OnToggleListener l);

    interface OnToggleListener {

        /**
         * @param isOpen isOpen
         */
        void onToggle(boolean isOpen);
    }
}
