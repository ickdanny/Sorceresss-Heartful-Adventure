import java.util.ArrayList;

public class System_EntityMovement implements ECSSystem {

    private static final ComponentFilter COMPFILTER = new ComponentFilter(new String[]{"position", "movement"});

    public System_EntityMovement(){
    }

    @Override
    public void update(ArrayList<Entity> entities){
        readBlackboard();
        for(Entity e : entities){
            Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
            Component_Movement mComp = (Component_Movement)ComponentManager.getComponent("movement", e.getEntityID());

            if(pComp == null || mComp == null){
                continue;
            }
            Vector cPos = pComp.getPosition();
            if(cPos == null){
                continue;
            }

            //move to promised position
            pComp.stepPositions();

            //calculate next position
            Vector vel = new Vector(0, 0);
            for (MoveModeAdd mm : mComp.getAddModes()) {
                vel = Vector.add(vel, this.add(mm, e));
            }
            for (MoveModeTrans mm : mComp.getTransModes()) {
                this.transform(cPos, vel, mm, e);
            }

            //set the next promised position
            pComp.setPosition(Vector.add(cPos, vel));
        }
    }

    private void readBlackboard(){
        for(BoardMessage bm : SystemManager.getECSBoard().getMessages()) {
            if (bm.getSender() == this) {
                switch (bm.getMessage()) {
                    case "clear_movement_modes":
                        clearMovementModes((String)bm.getData()[0]);
                        break;
                    case "remove_movement_mode":
                        removeMovementMode((String)bm.getData()[0], (MoveMode)bm.getData()[1]);
                        break;
                    case "add_movement_mode":
                        addMovementMode((String)bm.getData()[0], (MoveMode)bm.getData()[1]);
                        break;
                }
            }
        }
    }

    private void clearMovementModes(String id){
        Component_Movement mComp = ((Component_Movement) ComponentManager.getComponent("movement", id));
        if(mComp != null){
            mComp.clear();
        }
    }
    private void removeMovementMode(String id, MoveMode toRemove){
        Component_Movement mComp = ((Component_Movement) ComponentManager.getComponent("movement", id));
        if(mComp != null){
            mComp.removeMode(toRemove);
        }
    }
    private void addMovementMode(String id, MoveMode toAdd){
        Component_Movement mComp = ((Component_Movement) ComponentManager.getComponent("movement", id));
        if(mComp != null){
            mComp.addMode(toAdd);
        }
    }

    @Override
    public ComponentFilter getCompfilter(){
        return COMPFILTER;
    }

    //distribute to individual methods per MM
    //Entity e is the entity WITH the movementMode
    private Vector add(MoveModeAdd mm, Entity e){
        switch(mm.getMode()){
            case "player_movement":
                return addPlayerMovement(e);
            case "velocity":
                return addVelocity((MoveModeAddVector)mm);
            case "polar":
                return addPolar((MoveModeAddVector)mm);
            //will not bounce off the bottom, if i need that iw ill have another mode
            case "bounce":
                return addBounce((MoveModeAddBounce)mm, e);
            case "bounce_bottom":
                return addBounceBottom((MoveModeAddBounce)mm, e);
            //for the default gravity vector is in velocity form
            case "gravity":
                return addGravity((MoveModeAddGravity)mm);
            case "path":
                return addPath((MoveModeAddPath)mm, e);
            case "path_looping":
                return addPathLooping((MoveModeAddPath)mm, e);
            case "path_final_polar":
                return addPathFinalPolar((MoveModeAddPath)mm, e);
            case "point":
                return addPoint((MoveModeAddPoint)mm, e);
            case "orbit":
                return addOrbit((MoveModeAddOrbit)mm, e);
            case "orbit_recalc":
                return addOrbitRecalc((MoveModeAddOrbit)mm, e);
            case "orbit_variable_dist":
            case "orbit_variable_distance":
                return addOrbitVariableDist((MoveModeAddOrbitVariableDist)mm, e);
            case "orbit_recalc_variable_dist":
            case "orbit_recalc_variable_distance":
                return addOrbitRecalcVariableDist((MoveModeAddOrbitVariableDist)mm, e);
            case "orbit_angular_velocity":
                return addOrbitAngularVelocity((MoveModeAddOrbit)mm, e);
            case "orbit_recalc_angular_velocity":
                return addOrbitRecalcAngularVelocity((MoveModeAddOrbit)mm, e);
            case "orbit_angular_velocity_variable_dist":
            case "orbit_angular_velocity_variable_distance":
                return addOrbitAngularVelocityVariableDist((MoveModeAddOrbitVariableDist)mm, e);
            case "orbit_recalc_angular_velocity_variable_dist":
            case "orbit_recalc_angular_velocity_variable_distance":
                return addOrbitRecalcAngularVelocityVariableDist((MoveModeAddOrbitVariableDist)mm, e);
            case "angle_change":
                return addAngleChange((MoveModeAddAngleChange)mm);
            case "angle_change_compounding":
                return addAngleChangeCompounding((MoveModeAddAngleChange)mm);
            case "angle_change_incrementing":
                return addAngleChangeIncrementing((MoveModeAddAngleChange)mm);
            case "homing":
                return addHoming((MoveModeAddHoming)mm, e);
            case "homing_compounding":
                return addHomingCompounding((MoveModeAddHoming)mm, e);
            case "homing_incrementing":
                return addHomingIncrementing((MoveModeAddHoming)mm, e);
            //addFollowEntity is not homing: the entity is on top of the other one
            case "follow_entity":
                return addFollowEntity((MoveModeAddString)mm, e);
            //careful - this uses true positions
            case "follow_entity_closely":
                return addFollowEntityClosely((MoveModeAddString) mm, e);
            case "pickup_movement":
                return addPickupMovement((MoveModeAddDouble)mm);
            default:
                return new Vector(0, 0);
        }
    }

    //distribute to individual methods per MM
    private void transform(Vector pos, Vector base, MoveModeTrans mm, Entity e){
        switch(mm.getMode()){
            case "speed":
                transformSpeed((MoveModeTransDouble)mm, base);
                break;
            //vector is in format <SpeedMultiplier, CompoundMultiplier>
            case "speed_compounding":
                transformSpeedCompounding((MoveModeTransVector)mm, base);
                break;
            //vector is in format <SpeedMultiplier, SpeedAdd>
            case "speed_incrementing":
                transformSpeedIncrementing((MoveModeTransVector)mm, base);
                break;
            case "speed_incrementing_to_1":
                transformSpeedIncrementingTo1((MoveModeTransVector)mm, base);
                break;
            case "speed_compounding_to_1":
                transformSpeedCompoundingTo1((MoveModeTransVector)mm, base);
                break;

            case "player_bounds":
                transformPlayerBounds(pos, base);
                break;
        }
    }

    //famous last words: lets just make all the shot types move at the same speed
    //yeah pretty much i don't give a fuck
    private Vector addPlayerMovement(Entity player){
        Component_Player pComp = (Component_Player)(ComponentManager.getComponent("player", player.getEntityID()));
        if(!pComp.canInput()) {
            return new Vector(0, 0);
        }

        double baseSpeed = 7.3;
        double focusMultiplier = .3;

        //parse inputs
        boolean up, down, left, right, focused;
        InputTable it = GameDriver.getCurrentInputTable();
        String[] inputs = it.getInputs();
        up = inputs[5].equals("just pressed") || inputs[5].equals("still pressed");
        down = inputs[6].equals("just pressed") || inputs[6].equals("still pressed");
        left = inputs[3].equals("just pressed") || inputs[3].equals("still pressed");
        right = inputs[4].equals("just pressed") || inputs[4].equals("still pressed");
        focused = inputs[0].equals("just pressed") || inputs[0].equals("still pressed");

        //calculate direction
        Vector direction = new Vector(0, 0);
        if(up){
            direction.setB(direction.getB() - 1);
        }
        if(down){
            direction.setB(direction.getB() + 1);
        }
        if(left){
            direction.setA(direction.getA() - 1);
        }
        if(right){
            direction.setA(direction.getA() + 1);
        }

        if(direction.isZero()){
            return direction;
        }

        //set speed
        Vector toRet = Vector.normalize(direction);
        if(!focused){
            toRet = Vector.scalarMultiple(toRet, baseSpeed);
        }
        else{
            toRet = Vector.scalarMultiple(toRet, baseSpeed * focusMultiplier);
        }
        return toRet;
    }

    private Vector addVelocity(MoveModeAddVector mm){
        return mm.getVector();
    }
    private Vector addPolar(MoveModeAddVector mm){
        return Vector.toVelocity(mm.getVector());
    }

    private Vector addBounce(MoveModeAddBounce mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }
        Vector pos = pComp.getPosition();
        Vector vel = mm.getVector().deepClone();

        int bounds = 10;
        while(mm.getBouncesRemaining() > 0){

            //check top first
            if(pos.getB() <= bounds){
                vel.setB(-vel.getB());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                //hacky way to prevent clipping - pos IS the pcomp position
                pos.setB(bounds);
                break;
            }
            if(pos.getA() <= bounds){
                vel.setA(-vel.getA());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                pos.setA(bounds);
                break;
            }
            else if(pos.getA() >= (GameDriver.getGameWidth() - bounds)){
                vel.setA(-vel.getA());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                pos.setA(GameDriver.getGameWidth() - bounds);
                break;
            }
            //if none of the conditions are true we get out
            break;
        }
        mm.setVector(vel);
        return vel;
    }
    private Vector addBounceBottom(MoveModeAddBounce mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }
        Vector pos = pComp.getPosition();
        Vector vel = mm.getVector().deepClone();

        int bounds = 10;
        while(mm.getBouncesRemaining() > 0){

            //check top first
            if(pos.getB() <= bounds){
                vel.setB(-vel.getB());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                //hacky way to prevent clipping - pos IS the pcomp position
                pos.setB(bounds);
                break;
            }
            else if(pos.getB() >= (GameDriver.getGameHeight() - bounds)){
                vel.setB(-vel.getB());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                pos.setB(GameDriver.getGameHeight() - bounds);
                break;
            }
            if(pos.getA() <= bounds){
                vel.setA(-vel.getA());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                pos.setA(bounds);
                break;
            }
            else if(pos.getA() >= (GameDriver.getGameWidth() - bounds)){
                vel.setA(-vel.getA());
                mm.setBouncesRemaining(mm.getBouncesRemaining() - 1);
                pos.setA(GameDriver.getGameWidth() - bounds);
                break;
            }
            //if none of the conditions are true we get out
            break;
        }
        mm.setVector(vel);
        return vel;
    }

    private Vector addGravity(MoveModeAddGravity mm){
        Vector vel = mm.getVector();
        Vector gVel = Vector.toVelocity(new Vector(mm.getGAng(), mm.getGAccel()));
        Vector beforeDrag = Vector.add(vel, gVel);
        Vector tempPolar = Vector.toPolar(beforeDrag);
        double speed = tempPolar.getB();
        double afterDragSpeed = speed - (speed * mm.getDragMulti());
        //scale by the ratio of before/after drag
        //same effect if i just set the polar speed and returned to velocity
        Vector afterDrag = Vector.scalarMultiple(beforeDrag, afterDragSpeed/speed);
        mm.setVector(afterDrag);
        return afterDrag;
    }

    private Vector addPath(MoveModeAddPath mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        Vector currentPos = pComp.getPosition();
        Vector workingPos = currentPos.deepClone();
        ArrayList<Vector> points = mm.getPoints();
        int pIndex = mm.getNextPoint();
        double distR = mm.getSpeed();

        if(pIndex >= points.size() || pIndex < 0){
            return new Vector(0,0);
        }

        while(distR > 0){
            Vector nextPoint = points.get(pIndex);
            //calculate distance to the next point from the last point checked
            double distToNextPoint = Vector.getDistance(workingPos, nextPoint);
            //case 1: we run out of distance -> calculate the final point
            if(distR - distToNextPoint<= 0){
                double angleToNextPoint = Vector.getAngle(workingPos, nextPoint);
                workingPos = Vector.add(workingPos, Vector.toVelocity(new Vector(angleToNextPoint, distR)));
                return Vector.subtract(workingPos, currentPos);
            }
            //case 2: we don't run out of distance -> get to the next point
            else{
                //subtract from distR
                distR -= distToNextPoint;
                //move up
                ++pIndex;
                //case 1: end of path
                if(pIndex >= points.size()){
                    //remove this path
                    SystemManager.getECSBoard().write(new BoardMessage("remove_movement_mode", 2, this, new Object[]{e.getEntityID(), mm}));
                    return Vector.subtract(nextPoint, currentPos);
                }
                //case 2: move on to next point
                else{
                    workingPos = nextPoint.deepClone();
                    mm.goNextPoint();
                }
            }
        }
        return new Vector(0, 0);
    }
    private Vector addPathLooping(MoveModeAddPath mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        Vector currentPos = pComp.getPosition();
        Vector workingPos = currentPos.deepClone();
        ArrayList<Vector> points = mm.getPoints();
        int pIndex = mm.getNextPoint();
        double distR = mm.getSpeed();

        if(pIndex >= points.size() || pIndex < 0){
            return new Vector(0,0);
        }

        while(distR > 0){
            Vector nextPoint = points.get(pIndex);
            //calculate distance to the next point from the last point checked
            double distToNextPoint = Vector.getDistance(workingPos, nextPoint);
            //case 1: we run out of distance -> calculate the final point
            if(distR - distToNextPoint<= 0){
                double angleToNextPoint = Vector.getAngle(workingPos, nextPoint);
                workingPos = Vector.add(workingPos, Vector.toVelocity(new Vector(angleToNextPoint, distR)));
                return Vector.subtract(workingPos, currentPos);
            }
            //case 2: we don't run out of distance -> get to the next point
            else{
                //subtract from distR
                distR -= distToNextPoint;
                //move up
                ++pIndex;
                if(pIndex >= points.size()){
                    pIndex = 0;
                }
                workingPos = nextPoint.deepClone();
                mm.goNextPoint();
            }
        }
        return new Vector(0, 0);
    }
    private Vector addPathFinalPolar(MoveModeAddPath mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        Vector currentPos = pComp.getPosition();
        Vector workingPos = currentPos.deepClone();
        ArrayList<Vector> points = mm.getPoints();
        int pIndex = mm.getNextPoint();
        double distR = mm.getSpeed();

        if(pIndex >= points.size() || pIndex < 0){
            return new Vector(0,0);
        }

        while(distR > 0){
            Vector nextPoint = points.get(pIndex);
            //calculate distance to the next point from the last point checked
            double distToNextPoint = Vector.getDistance(workingPos, nextPoint);
            //case 1: we run out of distance -> calculate the final point
            if(distR - distToNextPoint<= 0){
                double angleToNextPoint = Vector.getAngle(workingPos, nextPoint);
                workingPos = Vector.add(workingPos, Vector.toVelocity(new Vector(angleToNextPoint, distR)));
                return Vector.subtract(workingPos, currentPos);
            }
            //case 2: we don't run out of distance -> get to the next point
            else{
                //subtract from distR
                distR -= distToNextPoint;
                //move up
                ++pIndex;
                //case 1: end of path
                //final polar - last vector is the new polar vector
                if(pIndex >= points.size() - 1){
                    //remove this path
                    SystemManager.getECSBoard().write(new BoardMessage("remove_movement_mode", 2, this, new Object[]{e.getEntityID(), mm}));
                    //add new polar path
                    SystemManager.getECSBoard().write(new BoardMessage("add_movement_mode", 2, this, new Object[]{e.getEntityID(),
                            new MoveModeAddVector("polar", points.get(points.size() - 1))}));
                    return Vector.subtract(nextPoint, currentPos);
                }
                //case 2: move on to next point
                else{
                    workingPos = nextPoint.deepClone();
                    mm.goNextPoint();
                }
            }
        }
        return new Vector(0, 0);
    }
    //must use speed -> time cannot be used
    private Vector addPoint(MoveModeAddPoint mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        Vector currentPos = pComp.getPosition();
        Vector destination = mm.getVector();
        double maxDist = mm.getSpeed();

        double distToDest = Vector.getDistance(currentPos, destination);
        //case 1 - we make it to the point
        if(distToDest <= maxDist){
            //remove this mode
            SystemManager.getECSBoard().write(new BoardMessage("remove_movement_mode", 2, this, new Object[]{e.getEntityID(), mm}));
            return Vector.subtract(destination, currentPos);
        }
        //case 2 - we make it to the point (maxDist) on the angle
        else{
            return Vector.toVelocity(new Vector(Vector.getAngle(currentPos, destination), maxDist));
        }
    }
    private Vector addOrbit(MoveModeAddOrbit mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        Vector lastPosition = mm.getLastPosition();
        //calculate the current relative position as lastPos
        //lastPos in distanceFromCenter, angleFromCenter format
        if(lastPosition == null){
            Vector thisPosition = new Vector(Vector.getDistance(mm.getCenter(), pComp.getPosition()),
                    Vector.getAngle(mm.getCenter(), pComp.getPosition()));
            mm.setLastPosition(thisPosition);
            lastPosition = mm.getLastPosition();
        }
        //just working with what's given from the orbit
        double dist = lastPosition.getA();
        double lastAngle = lastPosition.getB();
        //dist = R; 2piR
        double circumference = dist * 2 * Math.PI;
        double ratio = mm.getSpeed()/circumference;
        double degChange = 360 * ratio;
        //increasing angle = counterclockwise
        if(mm.isClockwise()){
            degChange *= -1;
        }
        Vector polarFromCenter = new Vector(lastAngle + degChange, dist);
        Vector newLastPosition = new Vector( dist, MiscUtil.fixAngle(lastAngle + degChange));
        Vector nextPos = Vector.add(mm.getCenter(), Vector.toVelocity(polarFromCenter));
        mm.setLastPosition(newLastPosition);

        return Vector.subtract(nextPos, pComp.getPosition());
    }
    //this mode does not use lastPosiition
    //does not play nice with moving centers
    //but can be transformed
    private Vector addOrbitRecalc(MoveModeAddOrbit mm, Entity e){
        mm.setLastPosition(null);
        return addOrbit(mm, e);
    }
    private Vector addOrbitVariableDist(MoveModeAddOrbitVariableDist mm, Entity e){
        Vector toRet = addOrbit(mm, e);
        mm.getLastPosition().setA(mm.getLastPosition().getA() + mm.getDistChange());
        return toRet;
    }
    private Vector addOrbitRecalcVariableDist(MoveModeAddOrbitVariableDist mm, Entity e){
        //for this one i have to copy paste the code because addOrbitVariableDist uses lastPosition to change the distance
        //and if i'm recalculating that won't work
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        //the recalculating block gets taken out of the null case

        //calculate the current relative position as lastPos
        //lastPos in distanceFromCenter, angleFromCenter format
        Vector thisPosition = new Vector(Vector.getDistance(mm.getCenter(), pComp.getPosition()),
                Vector.getAngle(mm.getCenter(), pComp.getPosition()));
        mm.setLastPosition(thisPosition);
        Vector lastPosition = mm.getLastPosition();

        //and now here I add the distChange
        double dist = lastPosition.getA() + mm.getDistChange();
        double lastAngle = lastPosition.getB();
        //dist = R; 2piR
        double circumference = dist * 2 * Math.PI;
        double ratio = mm.getSpeed()/circumference;
        double degChange = 360 * ratio;
        //increasing angle = counterclockwise
        if(mm.isClockwise()){
            degChange *= -1;
        }
        Vector polarFromCenter = new Vector(lastAngle + degChange, dist);
        Vector newLastPosition = new Vector( dist, MiscUtil.fixAngle(lastAngle + degChange));
        Vector nextPos = Vector.add(mm.getCenter(), Vector.toVelocity(polarFromCenter));
        mm.setLastPosition(newLastPosition);

        return Vector.subtract(nextPos, pComp.getPosition());
    }
    private Vector addOrbitAngularVelocity(MoveModeAddOrbit mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        Vector lastPosition = mm.getLastPosition();
        //calculate the current relative position as lastPos
        //lastPos in distanceFromCenter, angleFromCenter format
        if(lastPosition == null){
            Vector thisPosition = new Vector(Vector.getDistance(mm.getCenter(), pComp.getPosition()),
                    Vector.getAngle(mm.getCenter(), pComp.getPosition()));
            mm.setLastPosition(thisPosition);
            lastPosition = mm.getLastPosition();
        }
        //just working with what's given from the orbit
        double dist = lastPosition.getA();
        double lastAngle = lastPosition.getB();

        //angular velocity - speed = deg / tick
        double degChange = mm.getSpeed();
        //increasing angle = counterclockwise
        if(mm.isClockwise()){
            degChange *= -1;
        }
        Vector polarFromCenter = new Vector(lastAngle + degChange, dist);
        Vector newLastPosition = new Vector( dist, MiscUtil.fixAngle(lastAngle + degChange));
        Vector nextPos = Vector.add(mm.getCenter(), Vector.toVelocity(polarFromCenter));
        mm.setLastPosition(newLastPosition);

        return Vector.subtract(nextPos, pComp.getPosition());
    }
    private Vector addOrbitRecalcAngularVelocity(MoveModeAddOrbit mm, Entity e){
        mm.setLastPosition(null);
        return addOrbitAngularVelocity(mm, e);
    }
    private Vector addOrbitAngularVelocityVariableDist(MoveModeAddOrbitVariableDist mm, Entity e){
        Vector toRet = addOrbitAngularVelocity(mm, e);
        mm.getLastPosition().setA(mm.getLastPosition().getA() + mm.getDistChange());
        return toRet;
    }
    private Vector addOrbitRecalcAngularVelocityVariableDist(MoveModeAddOrbitVariableDist mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }

        //the recalculating block gets taken out of the null case

        //calculate the current relative position as lastPos
        //lastPos in distanceFromCenter, angleFromCenter format
        Vector thisPosition = new Vector(Vector.getDistance(mm.getCenter(), pComp.getPosition()),
                Vector.getAngle(mm.getCenter(), pComp.getPosition()));
        mm.setLastPosition(thisPosition);
        Vector lastPosition = mm.getLastPosition();

        //and now here I add the distChange
        double dist = lastPosition.getA() + mm.getDistChange();
        double lastAngle = lastPosition.getB();
        //dist = R; 2piR
        double degChange = mm.getSpeed();
        //increasing angle = counterclockwise
        if(mm.isClockwise()){
            degChange *= -1;
        }
        Vector polarFromCenter = new Vector(lastAngle + degChange, dist);
        Vector newLastPosition = new Vector( dist, MiscUtil.fixAngle(lastAngle + degChange));
        Vector nextPos = Vector.add(mm.getCenter(), Vector.toVelocity(polarFromCenter));
        mm.setLastPosition(newLastPosition);

        return Vector.subtract(nextPos, pComp.getPosition());
    }

    //angle change vector = polar
    //a = angle change
    //b = compound/incr
    private Vector addAngleChange(MoveModeAddAngleChange mm){
        Vector polar = mm.getVector().deepClone();
        mm.getVector().setA(MiscUtil.fixAngle(mm.getVector().getA() + mm.getA()));
        return Vector.toVelocity(polar);
    }
    private Vector addAngleChangeCompounding(MoveModeAddAngleChange mm){
        Vector polar = mm.getVector().deepClone();
        //angle adds a
        mm.getVector().setA(MiscUtil.fixAngle(mm.getVector().getA() + mm.getA()));
        //b compounds a //DONT FIX THE ANGLE HERE IT'S NOT AN ANGLE
        mm.setA(mm.getA() * mm.getB());
        return Vector.toVelocity(polar);
    }
    private Vector addAngleChangeIncrementing(MoveModeAddAngleChange mm){
        Vector polar = mm.getVector().deepClone();
        mm.getVector().setA(MiscUtil.fixAngle(mm.getVector().getA() + mm.getA()));
        mm.setA(mm.getA() + mm.getB());
        return Vector.toVelocity(polar);
    }
    private Vector addHoming(MoveModeAddHoming mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());

        Vector polar = mm.getVector().deepClone();
        if(pComp != null) {
            Vector pos = pComp.getPosition();
            double idealAngle = Vector.getAngle(pos, mm.getTarget());
            double ourAngle = polar.getA();
            double angleChange = mm.getA();

            double angleDiff = MiscUtil.angleDiff(ourAngle, idealAngle);
            if (angleDiff <= angleChange) {
                mm.getVector().setA(idealAngle);
//                return Vector.toVelocity(polar);
            } else {
                //turn correct direction
                int dir = MiscUtil.turnDirection(ourAngle, idealAngle);
                mm.getVector().setA(MiscUtil.fixAngle(mm.getVector().getA() + (mm.getA() * dir)));
            }
        }
        return Vector.toVelocity(polar);
    }
    private Vector addHomingCompounding(MoveModeAddHoming mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());

        Vector polar = mm.getVector().deepClone();
        if(pComp != null) {
            Vector pos = pComp.getPosition();
            double idealAngle = Vector.getAngle(pos, mm.getTarget());
            double ourAngle = polar.getA();
            double angleChange = mm.getA();

            double angleDiff = MiscUtil.angleDiff(ourAngle, idealAngle);
            if(angleDiff <= angleChange){
                mm.getVector().setA(idealAngle);
            }
            else {
                //turn correct direction
                int dir = MiscUtil.turnDirection(ourAngle, idealAngle);
                mm.getVector().setA(MiscUtil.fixAngle(mm.getVector().getA() + (mm.getA() * dir)));
            }

            //compound
            mm.setA(mm.getA() * mm.getB());
        }
        return Vector.toVelocity(polar);
    }
    private Vector addHomingIncrementing(MoveModeAddHoming mm, Entity e){
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());

        Vector polar = mm.getVector().deepClone();
        if(pComp != null) {
            Vector pos = pComp.getPosition();
            double idealAngle = Vector.getAngle(pos, mm.getTarget());
            double ourAngle = polar.getA();
            double angleChange = mm.getA();

            double angleDiff = MiscUtil.angleDiff(ourAngle, idealAngle);
            if(angleDiff <= angleChange){
                mm.getVector().setA(idealAngle);
//                return Vector.toVelocity(polar);
            }
            else {
                //turn correct direction
                int dir = MiscUtil.turnDirection(ourAngle, idealAngle);
                mm.getVector().setA(MiscUtil.fixAngle(mm.getVector().getA() + (mm.getA() * dir)));

            }
            //increment
            mm.setA(mm.getA() + mm.getB());
        }
        return Vector.toVelocity(polar);
    }

    private Vector addFollowEntity(MoveModeAddString mm, Entity me){
        Entity e = EntityManager.getEntity((mm).getString());
        if(e == null){
            return new Vector(0, 0);
        }
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }
        Component_Position myPos = (Component_Position)ComponentManager.getComponent("position", me.getEntityID());
        if(myPos == null){
            return new Vector(0, 0);
        }
        return Vector.subtract(pComp.getPrevPosition(), myPos.getPrevPosition());
    }
    //uses true positions
    private Vector addFollowEntityClosely(MoveModeAddString mm, Entity me){
        Entity e = EntityManager.getEntity((mm).getString());
        if(e == null){
            return new Vector(0, 0);
        }
        Component_Position pComp = (Component_Position)ComponentManager.getComponent("position", e.getEntityID());
        if(pComp == null){
            return new Vector(0, 0);
        }
        Component_Position myPos = (Component_Position)ComponentManager.getComponent("position", me.getEntityID());
        if(myPos == null){
            return new Vector(0, 0);
        }
        return Vector.subtract(pComp.getPosition(), myPos.getPosition());
    }

    private Vector addPickupMovement(MoveModeAddDouble mm){
        //the double is the returned y velocity - we increment it here
        double maximumYVel = 6;
        double yVelIncr = .13;
        double yVel = mm.getD();
        Vector toRet = new Vector(0, yVel);
        if(yVel < maximumYVel){
            yVel += yVelIncr;
            if(yVel > maximumYVel){
                yVel = maximumYVel;
            }
            mm.setD(yVel);
        }
        return toRet;
    }

    private void transformSpeed(MoveModeTransDouble mm, Vector base){
        if(base.isZero()){
            return;
        }
        if(mm.getD() == 0){
            base.copy(new Vector(0, 0));
            return;
        }
        double l = Vector.getLength(base);
        //divide by the length and multiply by the new speed
        base.copy(Vector.scalarMultiple(base, mm.getD()/l));
    }
    private void transformPlayerBounds(Vector pos, Vector base){
        double boundaryX = 17;
        double boundaryY = 30.5;
        Vector nextPosition = Vector.add(pos, base);
        if(nextPosition.getA() < boundaryX){
            nextPosition.setA(boundaryX);
        }
        else if(nextPosition.getA() > GameDriver.getGameWidth() - boundaryX){
            nextPosition.setA(GameDriver.getGameWidth() - boundaryX);
        }
        if(nextPosition.getB() < boundaryY){
            nextPosition.setB(boundaryY);
        }
        else if(nextPosition.getB() > GameDriver.getGameHeight() - boundaryY){
            nextPosition.setB(GameDriver.getGameHeight() - boundaryY);
        }

        Vector newBase = Vector.subtract(nextPosition, pos);
        base.copy(newBase);
    }
    //vector is in format <SpeedMultiplier, CompoundMultiplier>
    private void transformSpeedCompounding(MoveModeTransVector mm, Vector base){
        Vector v = mm.getVector();
        base.copy(Vector.scalarMultiple(base, v.getA()));
        mm.setVector(new Vector(v.getA() * v.getB(), v.getB()));
    }
    //vector is in format <SpeedMultiplier, SpeedAdd>
    private void transformSpeedIncrementing(MoveModeTransVector mm, Vector base){
        Vector v = mm.getVector();
        base.copy(Vector.scalarMultiple(base, v.getA()));
        mm.setVector(new Vector(v.getA() + v.getB(), v.getB()));
    }
    //vector is in format <speed multi, speed add>
    private void transformSpeedIncrementingTo1(MoveModeTransVector mm, Vector base){
        Vector v = mm.getVector();
        if(v.getA() >= 1){
            return;
        }
        base.copy(Vector.scalarMultiple(base, v.getA()));
        mm.setVector(new Vector(v.getA() + v.getB(), v.getB()));
    }
    //vector is in format <speed multi, speed compound>
    private void transformSpeedCompoundingTo1(MoveModeTransVector mm, Vector base){
        Vector v = mm.getVector();
        if(v.getA() >= 1){
            return;
        }
        base.copy(Vector.scalarMultiple(base, v.getA()));
        if(v.getA() != 0) {
            mm.setVector(new Vector(v.getA() * (v.getB() + 1), v.getB()));
        }
        else{
            mm.setVector(new Vector(v.getB()));
        }
    }

    public static ComponentFilter getCompFilter(){
        return COMPFILTER;
    }
}