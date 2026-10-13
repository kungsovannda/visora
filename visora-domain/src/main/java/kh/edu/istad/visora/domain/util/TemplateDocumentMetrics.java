package kh.edu.istad.visora.domain.util;

import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.vo.Component;
import kh.edu.istad.visora.domain.entity.vo.Page;
import kh.edu.istad.visora.domain.exception.template.TemplateValidationException;

import java.util.List;

/**
 * Computes the derived fields the backend is responsible for and normalizes the
 * document payload the frontend owns for a {@link Template}:
 * <ul>
 *     <li>pageCount from the number of submitted pages</li>
 *     <li>hasTimer from inspecting every component on every page</li>
 *     <li>canExport (false whenever a timer component exists, applies to PDF/SVG)</li>
 *     <li>layerIndex derived from the array order on each page</li>
 *     <li>timer durationMs range validation (1000 .. 86400000)</li>
 * </ul>
 */
public final class TemplateDocumentMetrics {

    public static final long MIN_TIMER_DURATION_MS = 1_000L;
    public static final long MAX_TIMER_DURATION_MS = 86_400_000L;
    private static final String TIMER_COMPONENT_TYPE = "COUNTDOWN_TIMER";

    private TemplateDocumentMetrics() {
    }

    public static void recompute(Template template) {
        List<Page> pages = template.getPages();
        if (pages == null || pages.isEmpty()) {
            template.setPageCount(0);
            template.setHasTimer(false);
            template.setCanExport(true);
            return;
        }

        template.setPageCount(pages.size());

        boolean hasTimer = false;
        for (Page page : pages) {
            List<Component> components = page.getComponents();
            if (components == null || components.isEmpty()) {
                continue;
            }
            for (int index = 0; index < components.size(); index++) {
                Component component = components.get(index);
                component.setLayerIndex(index);
                boolean isTimer = TIMER_COMPONENT_TYPE.equals(component.getType());
                if (isTimer) {
                    hasTimer = true;
                    validateTimerDuration(component);
                }
            }
        }

        template.setHasTimer(hasTimer);
        template.setCanExport(!hasTimer);
    }

    private static void validateTimerDuration(Component component) {
        if (component.getTimer() == null || component.getTimer().get("durationMs") == null) {
            return;
        }
        Object raw = component.getTimer().get("durationMs");
        if (!(raw instanceof Number number)) {
            throw new TemplateValidationException(
                    "timer.durationMs must be an integer between 1000 (one second) and 86400000 (24 hours)");
        }
        long durationMs = number.longValue();
        if (durationMs < MIN_TIMER_DURATION_MS || durationMs > MAX_TIMER_DURATION_MS) {
            throw new TemplateValidationException(
                    "timer.durationMs must be between 1000 (one second) and 86400000 (24 hours)");
        }
    }

}