/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package games.rednblack.editor.controller.commands.component;

import com.badlogic.ashley.core.Entity;
import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.controller.commands.EntityModifyRevertableCommand;
import games.rednblack.editor.renderer.components.light.LightObjectComponent;
import games.rednblack.editor.renderer.data.LightVO;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.h2d.common.MsgAPI;

public class UpdateLightDataCommand extends EntityModifyRevertableCommand {

    private Integer entityId;
    private LightVO backup;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();
        Entity entity = (Entity) payload[0];
        LightVO vo = (LightVO) payload[1];
        entityId = EntityUtils.getEntityId(entity);

        backup = new LightVO();
        backup.loadFromEntity(entity);

        LightObjectComponent lightObjectComponent = ComponentRetriever.get(entity, LightObjectComponent.class);

        lightObjectComponent.rays = vo.rays;
        lightObjectComponent.isStatic = vo.isStatic;
        lightObjectComponent.isXRay = vo.isXRay;
        lightObjectComponent.coneDegree = vo.coneDegree;
        lightObjectComponent.distance = vo.distance;
        lightObjectComponent.softnessLength = vo.softnessLength;
        lightObjectComponent.directionDegree = vo.directionDegree;
        lightObjectComponent.isSoft = vo.isSoft;
        lightObjectComponent.isActive = vo.isActive;

        HyperLap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    @Override
    public void undoAction() {
        Entity entity = EntityUtils.getByUniqueId(entityId);
        LightObjectComponent lightObjectComponent = ComponentRetriever.get(entity, LightObjectComponent.class);

        lightObjectComponent.rays = backup.rays;
        lightObjectComponent.isStatic = backup.isStatic;
        lightObjectComponent.isXRay = backup.isXRay;
        lightObjectComponent.coneDegree = backup.coneDegree;
        lightObjectComponent.distance = backup.distance;
        lightObjectComponent.softnessLength  = backup.softnessLength;
        lightObjectComponent.directionDegree = backup.directionDegree;
        lightObjectComponent.isActive = backup.isActive;
        lightObjectComponent.isSoft = backup.isSoft;

        HyperLap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    public static Object payload(Entity entity, LightVO vo) {
        Object[] payload = new Object[2];
        payload[0] = entity;
        payload[1] = vo;

        return payload;
    }
}
