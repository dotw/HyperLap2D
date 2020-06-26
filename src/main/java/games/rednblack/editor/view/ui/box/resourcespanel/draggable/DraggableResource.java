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

package games.rednblack.editor.view.ui.box.resourcespanel.draggable;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import games.rednblack.editor.utils.StandardWidgetsFactory;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.box.BoxItemResource;
import games.rednblack.h2d.common.ResourcePayloadObject;
import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.proxy.ResourceManager;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;

import java.util.function.BiFunction;

/**
 * Created by azakhary on 7/3/2014.
 */
public class DraggableResource extends DragAndDrop {

    protected final Sandbox sandbox;
    private final DraggableResourceView viewComponent;
    private BiFunction<String, Vector2, Boolean> factoryFunction;

    public DraggableResource(DraggableResourceView viewComponent) {
        this.viewComponent = viewComponent;
        sandbox = Sandbox.getInstance();
    }

    public void initDragDrop() {
        setKeepWithinStage(false);

        addSource(new Source((Actor) viewComponent) {
            public Payload dragStart(InputEvent event, float x, float y, int pointer) {
                Payload payload = new Payload();
                Actor dragActor = viewComponent.getDragActor();
                OrthographicCamera runtimeCamera = Sandbox.getInstance().getCamera();
                dragActor.setScale(1f/runtimeCamera.zoom);

                ResourcePayloadObject payloadData = viewComponent.getPayloadData();
                payloadData.xOffset = dragActor.getWidth() / 2f;
                payloadData.yOffset = dragActor.getHeight() / 2f;
                payload.setDragActor(dragActor);
                payload.setObject(payloadData);
                payload.setInvalidDragActor(null);
                float dragX = dragActor.getWidth() - (dragActor.getWidth() / (runtimeCamera.zoom * 2f));
                float dragY = dragActor.getHeight() / (runtimeCamera.zoom * 2f);
                setDragActorPosition(dragX, -dragY);
                return payload;
            }
        });

        addTarget(new Target(sandbox.getUIStage().dummyTarget) {
            @Override
            public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) {
                Vector2 vector = sandbox.screenToWorld(x, y);
                DraggableResource.this.drop(payload, vector);
            }
        });


        HyperLap2DFacade facade = HyperLap2DFacade.getInstance();
        UIResourcesBoxMediator resourcesBoxMediator = facade.retrieveMediator(UIResourcesBoxMediator.NAME);
        for (Target t : resourcesBoxMediator.customTargets) {
            addTarget(t);
        }

        if (viewComponent instanceof BoxItemResource) {
            StandardWidgetsFactory.addVisTooltip((Actor) viewComponent, viewComponent.getPayloadData().name);
        }
    }

    private void drop(Payload payload, Vector2 vector2) {
        ResourcePayloadObject resourcePayloadObject = (ResourcePayloadObject) payload.getObject();
        ResourceManager resourceManager = HyperLap2DFacade.getInstance().retrieveProxy(ResourceManager.NAME);

        vector2.sub(resourcePayloadObject.xOffset/resourceManager.getProjectVO().pixelToWorld, resourcePayloadObject.yOffset/resourceManager.getProjectVO().pixelToWorld);
        factoryFunction.apply(resourcePayloadObject.name, vector2);
    }

    public DraggableResourceView getViewComponent() {
        return viewComponent;
    }

    public void setFactoryFunction(BiFunction<String, Vector2, Boolean> factoryFunction) {
        this.factoryFunction = factoryFunction;
    }
}
