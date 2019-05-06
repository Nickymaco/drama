package org.drama.exception;

import static org.drama.text.MessageText.ELEM_HANDING_ERROR;
import static org.drama.text.MessageText.EMPTY_ELEMENS_MSG;
import static org.drama.text.MessageText.EMPTY_EVENT_MSG;
import static org.drama.text.MessageText.ILLEGAL_BROADCAST_MSG;
import static org.drama.text.MessageText.ILLEGAL_EVENT_MSG;
import static org.drama.text.MessageText.ILLEGAL_LAYER_DESC;
import static org.drama.text.MessageText.ILLEGAL_LAYER_DESCRIPTOR;
import static org.drama.text.MessageText.NO_SPECIAL_LAYER_PROP_MSG;
import static org.drama.text.MessageText.PLAYE_RROR_MSG;
import static org.drama.text.MessageText.format;

import java.util.Objects;

import org.drama.annotation.LayerDescription;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.core.LayerDescriptor;
import org.drama.event.Event;

public class DramaException extends RuntimeException {
   

    private static final long serialVersionUID = -7337990653787626209L;

    public DramaException(String message) {
        super(message);
    }

    public DramaException(Throwable e, String message) {
        super(message, e);
    }


    public static DramaException emptyRegisterEvents() {
        return new DramaException(EMPTY_EVENT_MSG);
    }

    public static DramaException emptyRegisterElements() {
        return new DramaException(EMPTY_ELEMENS_MSG);
    }

    public static DramaException occurredPlayError(Throwable e, Event event) {
        return new DramaException(e, PLAYE_RROR_MSG);
    }

    public static DramaException illegalRegisterEvent(Event event) {
        return new DramaException(format(ILLEGAL_EVENT_MSG, event));
    }

    public static DramaException illegalBroadcastEvent(Layer layer, Event event) {
        String msg = format(ILLEGAL_BROADCAST_MSG, layer);
        return Objects.isNull(event) ? new DramaException(new NullPointerException(), msg) : new DramaException(msg);
    }

    public static DramaException occurredHandingError(Throwable e, Element elem) {
        return new DramaException(e, format(ELEM_HANDING_ERROR, elem));
    }

    public static DramaException noSpecialLayerProp(Class<? extends Layer> layer) {
        return new DramaException(format(NO_SPECIAL_LAYER_PROP_MSG, layer.getName()));
    }

    public static DramaException illegalLayerDesc(LayerDescription layerDesc) {
        return new DramaException(format(ILLEGAL_LAYER_DESC, layerDesc.desc().getName(), layerDesc.target()));
    }

    public static DramaException illegalLayerDesc(LayerDescriptor desc) {
        return new DramaException(format(ILLEGAL_LAYER_DESCRIPTOR, desc.getUUID(), desc.getName()));
    }
}
