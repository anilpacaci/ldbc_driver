package com.ldbc.driver.workloads.ldbc.snb.interactive.db;

import com.google.common.collect.Lists;
import com.ldbc.driver.*;
import com.ldbc.driver.workloads.ldbc.snb.interactive.*;

import java.io.IOException;
import java.util.Map;

public class DummyLdbcSnbInteractiveDb extends Db {
    private static class DummyDbConnectionState extends DbConnectionState {
        @Override
        public void close() throws IOException {
        }
    }

    public static enum SleepType {
        THREAD_SLEEP,
        SPIN
    }

    public static final String SLEEP_DURATION_NANO_ARG = "ldbc.snb.interactive.db.sleep_duration_nano";
    public static final String SLEEP_TYPE_ARG = "ldbc.snb.interactive.db.sleep_type";

    private static long sleepDurationAsNano;
    private SleepType sleepType;

    private static interface SleepFun {
        void sleep(long sleepNs);
    }

    private static SleepFun sleepFun;

    @Override
    protected void onInit(Map<String, String> properties) throws DbException {
        String sleepDurationAsNanoAsString = properties.get(SLEEP_DURATION_NANO_ARG);
        if (null == sleepDurationAsNanoAsString) {
            sleepDurationAsNano = 0l;
        } else {
            try {
                sleepDurationAsNano = Long.parseLong(sleepDurationAsNanoAsString);
            } catch (NumberFormatException e) {
                throw new DbException(String.format("Error encountered while trying to parse value [%s] for %s", sleepDurationAsNanoAsString, SLEEP_DURATION_NANO_ARG), e);
            }
        }
        String sleepTypeString = properties.get(SLEEP_TYPE_ARG);
        if (null == sleepTypeString) {
            sleepType = SleepType.SPIN;
        } else {
            try {
                sleepType = SleepType.valueOf(properties.get(SLEEP_TYPE_ARG));
            } catch (IllegalArgumentException e) {
                throw new DbException(String.format("Invalid sleep type: %s", sleepTypeString));
            }
        }

        if (0 == sleepDurationAsNano) {
            sleepFun = new SleepFun() {
                @Override
                public void sleep(long sleepNs) {
                    // do nothing
                }
            };
        } else {
            switch (sleepType) {
                case THREAD_SLEEP:
                    sleepFun = new SleepFun() {
                        @Override
                        public void sleep(long sleepNs) {
                            try {
                                Thread.sleep(sleepNs / 1000000);
                            } catch (InterruptedException e) {
                                // do nothing
                            }
                        }
                    };
                    break;
                case SPIN:
                    sleepFun = new SleepFun() {
                        @Override
                        public void sleep(long sleepNs) {
                            long endTimeAsNano = System.nanoTime() + sleepNs;
                            while (System.nanoTime() < endTimeAsNano) {
                                // busy wait
                            }
                        }
                    };
                    break;
            }
        }

        properties.put(SLEEP_DURATION_NANO_ARG, Long.toString(sleepDurationAsNano));
        properties.put(SLEEP_TYPE_ARG, sleepType.name());

        registerOperationHandler(LdbcQuery1.class, LdbcQuery1ToNothing.class);
        registerOperationHandler(LdbcQuery2.class, LdbcQuery2ToNothing.class);
        registerOperationHandler(LdbcQuery3.class, LdbcQuery3ToNothing.class);
        registerOperationHandler(LdbcQuery4.class, LdbcQuery4ToNothing.class);
        registerOperationHandler(LdbcQuery5.class, LdbcQuery5ToNothing.class);
        registerOperationHandler(LdbcQuery6.class, LdbcQuery6ToNothing.class);
        registerOperationHandler(LdbcQuery7.class, LdbcQuery7ToNothing.class);
        registerOperationHandler(LdbcQuery8.class, LdbcQuery8ToNothing.class);
        registerOperationHandler(LdbcQuery9.class, LdbcQuery9ToNothing.class);
        registerOperationHandler(LdbcQuery10.class, LdbcQuery10ToNothing.class);
        registerOperationHandler(LdbcQuery11.class, LdbcQuery11ToNothing.class);
        registerOperationHandler(LdbcQuery12.class, LdbcQuery12ToNothing.class);
        registerOperationHandler(LdbcQuery13.class, LdbcQuery13ToNothing.class);
        registerOperationHandler(LdbcQuery14.class, LdbcQuery14ToNothing.class);
        registerOperationHandler(LdbcUpdate1AddPerson.class, LdbcUpdate1AddPersonToNothing.class);
        registerOperationHandler(LdbcUpdate2AddPostLike.class, LdbcUpdate2AddPostLikeToNothing.class);
        registerOperationHandler(LdbcUpdate3AddCommentLike.class, LdbcUpdate3AddCommentLikeToNothing.class);
        registerOperationHandler(LdbcUpdate4AddForum.class, LdbcUpdate4AddForumToNothing.class);
        registerOperationHandler(LdbcUpdate5AddForumMembership.class, LdbcUpdate5AddForumMembershipToNothing.class);
        registerOperationHandler(LdbcUpdate6AddPost.class, LdbcUpdate6AddPostToNothing.class);
        registerOperationHandler(LdbcUpdate7AddComment.class, LdbcUpdate7AddCommentToNothing.class);
        registerOperationHandler(LdbcUpdate8AddFriendship.class, LdbcUpdate8AddFriendshipToNothing.class);
    }

    @Override
    protected void onClose() throws IOException {
    }

    @Override
    protected DbConnectionState getConnectionState() throws DbException {
        return null;
    }

    private static void sleep(long sleepNs) {
        sleepFun.sleep(sleepNs);
    }

    public static class LdbcQuery1ToNothing extends OperationHandler<LdbcQuery1, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery1 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read1Result()));
        }
    }

    public static class LdbcQuery2ToNothing extends OperationHandler<LdbcQuery2, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery2 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read2Result()));
        }
    }

    public static class LdbcQuery3ToNothing extends OperationHandler<LdbcQuery3, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery3 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read3Result()));
        }
    }


    public static class LdbcQuery4ToNothing extends OperationHandler<LdbcQuery4, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery4 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read4Result()));
        }
    }

    public static class LdbcQuery5ToNothing extends OperationHandler<LdbcQuery5, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery5 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read5Result()));
        }
    }

    public static class LdbcQuery6ToNothing extends OperationHandler<LdbcQuery6, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery6 operation, DummyDbConnectionState dummyDbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read6Result()));
        }
    }

    public static class LdbcQuery7ToNothing extends OperationHandler<LdbcQuery7, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery7 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read7Result()));
        }
    }

    public static class LdbcQuery8ToNothing extends OperationHandler<LdbcQuery8, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery8 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read8Result()));
        }
    }

    public static class LdbcQuery9ToNothing extends OperationHandler<LdbcQuery9, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery9 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read9Result()));
        }
    }

    public static class LdbcQuery10ToNothing extends OperationHandler<LdbcQuery10, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery10 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read10Result()));
        }
    }

    public static class LdbcQuery11ToNothing extends OperationHandler<LdbcQuery11, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery11 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read11Result()));
        }
    }

    public static class LdbcQuery12ToNothing extends OperationHandler<LdbcQuery12, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery12 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read12Result()));
        }
    }

    public static class LdbcQuery13ToNothing extends OperationHandler<LdbcQuery13, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery13 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read13Result()));
        }
    }

    public static class LdbcQuery14ToNothing extends OperationHandler<LdbcQuery14, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcQuery14 operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, Lists.newArrayList(DummyLdbcSnbInteractiveOperationResultInstances.read14Result()));
        }
    }

    public static class LdbcUpdate1AddPersonToNothing extends OperationHandler<LdbcUpdate1AddPerson, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate1AddPerson operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate2AddPostLikeToNothing extends OperationHandler<LdbcUpdate2AddPostLike, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate2AddPostLike operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate3AddCommentLikeToNothing extends OperationHandler<LdbcUpdate3AddCommentLike, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate3AddCommentLike operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate4AddForumToNothing extends OperationHandler<LdbcUpdate4AddForum, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate4AddForum operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate5AddForumMembershipToNothing extends OperationHandler<LdbcUpdate5AddForumMembership, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate5AddForumMembership operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate6AddPostToNothing extends OperationHandler<LdbcUpdate6AddPost, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate6AddPost operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate7AddCommentToNothing extends OperationHandler<LdbcUpdate7AddComment, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate7AddComment operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }

    public static class LdbcUpdate8AddFriendshipToNothing extends OperationHandler<LdbcUpdate8AddFriendship, DummyDbConnectionState> {
        @Override
        public OperationResultReport executeOperation(LdbcUpdate8AddFriendship operation, DummyDbConnectionState dbConnectionState) throws DbException {
            sleep(sleepDurationAsNano);
            return operation.buildResult(0, null);
        }
    }
}
