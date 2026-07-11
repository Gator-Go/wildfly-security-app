
package com.swBuilder.security.app.event;

import com.swBuilder.security.app.entity.Movement;
import com.swBuilder.security.app.event.msg.CheckMaxVideosEvent;
import com.swBuilder.security.app.service.MovementService;
import com.swBuilder.security.app.settings.SecurityProperty;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ejb.Startup;
import jakarta.ejb.Singleton;

@Singleton
@Startup
@ApplicationScoped
public class CheckMaxVideosObserver {

    @Inject
    private Logger log;

    @Inject
    @SecurityProperty(name = "Max.Videos")
    private Integer MaxVideos;

    @Inject
    private MovementService movementService;

    public void onMaxVideosEvent(@Observes CheckMaxVideosEvent checkMaxVideosEvent) {

        List<Movement> movements = movementService.findAllMovements();
	int max = MaxVideos.intValue();
	int movementsSize = movements.size();

        log.info("onMaxVideosEvent:CheckMaxVideosEvent fired - max videos = " + max);
        log.info("onMaxVideosEvent:CheckMaxVideosEvent number of movements = " + movementsSize);

	if (movementsSize > max) {
	    Collections.reverse(movements);
            for (Movement movement : movements) {
		if (movement.getSave().booleanValue() == false) {
		    movementService.deleteMovement(movement.getId());
		    movementsSize--;
		    if (movementsSize <= max)
			break;
		}
            }
	}

    }
}
