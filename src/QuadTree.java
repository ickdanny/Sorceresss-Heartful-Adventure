import java.util.ArrayList;
import java.util.Iterator;

public class QuadTree {

    private static int MAX_OBJECTS = 8;
    private static int MAX_LEVELS = 5;

    private int level;

    private QuadTree[] nodes = new QuadTree[4];
    private AABB bounds;

    private ArrayList<Container> containers;

    public QuadTree(int level, AABB bounds){
        this.level = level;
        this.bounds = bounds;
        containers = new ArrayList<>();
    }

    //if inserted = true just check collisions
    //baseList = list of entities that collide with e
    public void insert(Entity e, ArrayList<Entity> baseList, boolean inserted){
        String ID = e.getEntityID();
        Container container = new Container((Component_Position)ComponentManager.getComponent("position", ID),
                (Component_Collision)ComponentManager.getComponent("collision", ID),
                (Component_Type)ComponentManager.getComponent("type", ID), e);

        //check collisions on this level
        for(Container c : containers){
            //filter out by type
            EntityType typeCheck = container.tComp.getType();
            EntityType typeAgainst = c.tComp.getType();
            if(!typeCollides(typeCheck, typeAgainst)){
                continue;
            }

            //filter out in case entity is already in baseList (different levels)
            if(baseList.contains(c.e)){
                continue;
            }

            //check collisions
            Component_Collision ccCheck = container.cComp;
            Component_Collision ccAgainst = c.cComp;

            AABB twoFrameCheck = ccCheck.getTwoFrame();
            AABB twoFrameAgainst = ccAgainst.getTwoFrame();

            //check two frame, then true
            if(AABBcollide(twoFrameCheck, twoFrameAgainst)){
                AABB trueCheck = ccCheck.getTruePos();
                AABB trueAgainst = ccAgainst.getTruePos();
                if(AABBcollide(trueCheck, trueAgainst)){
                    baseList.add(c.e);
                }
                else{
                    //run in-betweens based on largest speedRatio
                    double largestSpeedRatio = Math.max(speedRatio(trueCheck, twoFrameCheck),
                            speedRatio(trueAgainst, twoFrameAgainst));

                    if(largestSpeedRatio > 2){
                        int numChecks = (int)(largestSpeedRatio - 1);

                        Component_Position pCheck = container.pComp;
                        Component_Position pAgainst = c.pComp;
                        Vector oldCheck = pCheck.getOldPosition();
                        Vector newCheck = pCheck.getPrevPosition();
                        Vector oldAgainst = pAgainst.getOldPosition();
                        Vector newAgainst = pAgainst.getPrevPosition();

                        for(int i = 1; i <= numChecks; i++){
                            //1 check = split 2, 2 check = split 3, etc
                            double baseFraction = 1d/(numChecks + 1);
                            double currentFraction = baseFraction * i;

                            //interpolated centers
                            Vector interpolateCheck = Vector.interpolate(oldCheck, newCheck, currentFraction);
                            Vector interpolateAgainst = Vector.interpolate(oldAgainst, newAgainst, currentFraction);

                            //interpolated true hitboxes
                            AABB checkBox = AABB.calculateTruePos(interpolateCheck, ccCheck.getHitbox());
                            AABB againstBox = AABB.calculateTruePos(interpolateAgainst, ccAgainst.getHitbox());

                            //1 hit is enough
                            if(AABBcollide(checkBox, againstBox)){
                                baseList.add(c.e);
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(!inserted) {
            //check if need to split
            if (nodes[0] == null) {
                if (containers.size() > MAX_OBJECTS) {
                    split();
                }
                //if splitting, will have different criteria for inserting
                else {
                    //check if in bounds.
                    if (AABBcollide(bounds, container.cComp.getTwoFrame())) {
                        containers.add(container);
                    }
                    //switch inserted regardless
                    inserted = true;
                }
            }
        }

        //if has lower levels, check which levels need to check
        //this is for collision checking, will check insertion for each case
        //i.e. runs regardless of insertion
        if (nodes[0] != null) {
            boolean[] collidesNode = new boolean[4];
            int numNodesCollided = 0;

            //check collisions
            for(int i = 0;  i < 4; i++) {
                if (AABBcollide(container.cComp.getTwoFrame(), nodes[i].bounds)) {
                    collidesNode[i] = true;
                    numNodesCollided++;
                } else {
                    collidesNode[i] = false;
                }
            }

            //cases:
            //case out of bounds: do nothing
            //case 1 node collided, insert into that node
            if(numNodesCollided == 1){
                for(int i = 0; i < 4; i++){
                    if (collidesNode[i]){
                        nodes[i].insert(e, baseList, inserted);
                    }
                }
            }
            //if >1 node collided, insert into this node, run collisions on every colliding node
            else{
                if(!inserted) {
                    containers.add(container);
                    inserted = true;
                }
                for(int i = 0; i < 4; i++){
                    if (collidesNode[i]){
                        nodes[i].insert(e, baseList, inserted);
                    }
                }
            }
        }
    }

    private void split(){
        if(level >= MAX_LEVELS){
            return;
        }

        double xlow = bounds.getXlow();
        double xhigh = bounds.getXhigh();
        double ylow = bounds.getYlow();
        double yhigh = bounds.getYhigh();
        double xavg = (xlow + xhigh)/2;
        double yavg = (ylow + yhigh)/2;

        nodes[0] = new QuadTree(level + 1, new AABB(xlow, xavg, ylow, yavg));
        nodes[1] = new QuadTree(level + 1, new AABB(xavg, xhigh, ylow, yavg));
        nodes[2] = new QuadTree(level + 1, new AABB(xavg, xhigh, yavg, yhigh));
        nodes[3] = new QuadTree(level + 1, new AABB(xlow, xavg, yavg, yhigh));

        //distribute containers
        Iterator<Container> iter = containers.iterator();
        while(iter.hasNext()){
            Container c = iter.next();
            AABB twoFrame = c.cComp.getTwoFrame();
            boolean[] collidesNode = new boolean[4];
            int numNodesCollided = 0;

            //check collisions
            for(int i = 0;  i < 4; i++) {
                if (AABBcollide(twoFrame, nodes[i].bounds)) {
                    collidesNode[i] = true;
                    numNodesCollided++;
                } else {
                    collidesNode[i] = false;
                }
            }

            //case out of bounds: remove (this should never happen)
            if(numNodesCollided == 0){
                iter.remove();
            }
            //case distributable: distribute
            else if(numNodesCollided == 1){
                iter.remove();
                for(int i = 0; i < 4; i++){
                    if (collidesNode[i]){
                        nodes[i].containers.add(c);
                    }
                }
            }
            //case undistributable: do nothing (container stays within current level)
        }
    }

    public int getLevel() {
        return level;
    }
    public int getLowestLevel(){
        if(nodes[0] != null){
            return Math.max(Math.max(nodes[0].getLowestLevel(), nodes[1].getLowestLevel()),
                    Math.max(nodes[2].getLowestLevel(), nodes[3].getLowestLevel()));
        }
        return level;
    }
    public QuadTree[] getNodes() {
        return nodes;
    }
    public AABB getBounds() {
        return bounds;
    }

    private boolean typeCollides(EntityType a, EntityType b){
        switch(a) {
            case PLAYER:
                return b == EntityType.ENEMY || b == EntityType.BOSS || b == EntityType.ENEMY_P || b == EntityType.PICKUP;
            case PLAYER_P:
                return b == EntityType.ENEMY || b == EntityType.BOSS;
            case PLAYER_B:
                return b == EntityType.ENEMY || b == EntityType.BOSS || b == EntityType.ENEMY_P;
            case ENEMY:
            case BOSS:
                return b == EntityType.PLAYER || b == EntityType.PLAYER_P || b == EntityType.PLAYER_B;
            case ENEMY_P:
                return b == EntityType.PLAYER || b == EntityType.PLAYER_B;
            case PICKUP:
                return b == EntityType.PLAYER;
            default:
                return false;
        }
    }
    //true positions only
    private boolean AABBcollide(AABB a, AABB b){
        //check all four dimentions
        return a.getXlow() <= b.getXhigh()
                && a.getXhigh() >= b.getXlow()
                && a.getYlow() <= b.getYhigh()
                && a.getYhigh() >= b.getYlow();
    }
    //larger = two frame larger than real
    //1 = no movement
    private double speedRatio(AABB real, AABB two){
        //move to simple area ratio; square root is accurate for diagonal but not vertical
        return two.getArea()/real.getArea();
    }

    public String distributionReport(){
        int[] distribution = new int[getLowestLevel() + 1];
        distributionReport(distribution);

        String toRet = "";
        for(int i = 0; i < distribution.length; i++){
            toRet = toRet.concat(distribution[i] + ", ");
        }
        return toRet.substring(0, toRet.length() - 2);
    }
    private void distributionReport(int[] toRet){
        //add your current level
        toRet[level] += containers.size();
        //all children add
        if(nodes[0] != null) {
            for (int i = 0; i < 4; i++) {
                nodes[i].distributionReport(toRet);
            }
        }
    }

    private static class Container{
        private Component_Position pComp;
        private Component_Collision cComp;
        private Component_Type tComp;
        private Entity e;

        public Container(Component_Position pComp, Component_Collision cComp, Component_Type tComp, Entity e) {
            this.pComp = pComp;
            this.cComp = cComp;
            this.tComp = tComp;
            this.e = e;
        }
    }
}
