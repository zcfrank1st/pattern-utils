public class Demo {
    private String hello;
    private String world;

    public String getHello() {
        return hello;
    }

    public String getWorld() {
        return world;
    }

    private Demo(Builder builder) {
        this.hello = builder.hello;
        this.world = builder.world;
    }

    static class Builder {
        private String hello;
        private String world;

        public Builder setHello(String hello) {
            this.hello = hello;
            return this;
        }

        public Builder setWorld(String world) {
            this.world = world;
            return this;
        }

        public Demo build() {
            return new Demo(this);
        }
    }
}
