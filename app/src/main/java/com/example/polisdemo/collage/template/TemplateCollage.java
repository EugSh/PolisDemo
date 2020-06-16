package com.example.polisdemo.collage.template;

import com.example.polisdemo.collage.utils.MovementUtils;
import com.example.polisdemo.collage.utils.Utils;
import com.example.polisdemo.collage.view.CardView;

import java.util.List;

public class TemplateCollage {
    private final int width;
    private final int height;
    private final int margin = 20;

    public TemplateCollage(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void process(final List<CardView> cardViews) {
        switch (cardViews.size()) {
//            case 1:
//                process_1(cardViews);
//                break;
//            case 2:
//                process_2(cardViews);
//                break;
//            case 3:
//                process_3(cardViews);
//                break;
            case 4:
                process_4(cardViews);
                break;
            default:
                process_random(cardViews);
        }
    }

    private void process_4(final List<CardView> cardViews) {
        TransformInfo info = new TransformInfo();
        info.deltaScale = 0.5f;
        info.maximumScale = 0.5f;
        info.maximumScale = 2f;
        info.deltaX = -230;
        info.deltaY = -300;
        MovementUtils.move(cardViews.get(1), info);
        info.deltaX = 280;
        info.deltaY = -240;
        MovementUtils.move(cardViews.get(3), info);
        info.deltaX = 225;
        info.deltaY = 270;
        MovementUtils.move(cardViews.get(0), info);
        info.deltaX = -290;
        info.deltaY = 200;
        MovementUtils.move(cardViews.get(2), info);
    }

    private void process_1(final List<CardView> cardViews) {
        TransformInfo info = new TransformInfo(margin,
                margin,
                0,
                90,
                0,
                0,
                1f,
                1f);
        MovementUtils.move(cardViews.get(0), info);
    }

    private void process_2(final List<CardView> cardViews) {
        TransformInfo info = new TransformInfo(-150,
                margin,
                0,
                90,
                0,
                0,
                1f,
                1f);
        MovementUtils.move(cardViews.get(0), info);
        info.deltaX = width / 2 - 150;
        MovementUtils.move(cardViews.get(1), info);
    }

    private void process_3(final List<CardView> cardViews) {
        TransformInfo info = new TransformInfo(0,
                -100,
                0,
                90,
                0,
                0,
                1f,
                1f);
        System.out.println(info.toString());
        MovementUtils.move(cardViews.get(0), info);
        info.deltaX = margin;
        info.deltaY = height / 2 - 200;
        System.out.println(info.toString());
        MovementUtils.move(cardViews.get(1), info);
        info.deltaX = width / 2 - 200;
        System.out.println(info.toString());
        MovementUtils.move(cardViews.get(2), info);
    }

    private void process_random(final List<CardView> cardViews) {
        cardViews.forEach(c -> {
            TransformInfo randomInfo = new TransformInfo(Utils.generatRandomPositiveNegitiveValue(500, 0),
                    Utils.generatRandomPositiveNegitiveValue(500, 0),
                    Utils.generatRandomPositiveNegitiveValue(1, 0),
                    Utils.generatRandomPositiveNegitiveValue(22, 0),
                    Utils.generatRandomPositiveNegitiveValue(60, 0),
                    Utils.generatRandomPositiveNegitiveValue(60, 0),
                    0.5f,
                    1f);
            MovementUtils.move(c, randomInfo);
        });
    }
}
