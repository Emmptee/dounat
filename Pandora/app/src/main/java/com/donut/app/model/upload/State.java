package com.donut.app.model.upload;


/**
 * Created by Administrator on 2016/9/21.
 */

enum State {
    WAITING(0),
    STARTED(1),
    LOADING(2),
    FAILURE(3),
    CANCELLED(4),
    SUCCESS(5);

    private int value = 0;

    private State(int value) {
        this.value = value;
    }

    public static State valueOf(int value) {
        switch (value) {
            case 0:
                return WAITING;
            case 1:
                return STARTED;
            case 2:
                return LOADING;
            case 3:
                return FAILURE;
            case 4:
                return CANCELLED;
            case 5:
                return SUCCESS;
            default:
                return FAILURE;
        }
    }

    public int value() {
        return this.value;
    }
}
