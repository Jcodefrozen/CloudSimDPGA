public enum TestCaseSizes {
        One(1000, "Container1000"),
        Two(2000, "Container2000"),
        Three(2500, "Container2500"),
        Four(4000, "Container4000"),
        Five(5000, "Container5000"),
        Six(10000, "Container10000"),
        Seven(7000, "Container7000");


        private int testSize;
        private String name;
        TestCaseSizes(int testSize, String name){
                this.testSize = testSize;
                this.name = name;
        }
        public int getTestSize(){
                return testSize;
        }
        public String getName(){
                return name;
        }
}
