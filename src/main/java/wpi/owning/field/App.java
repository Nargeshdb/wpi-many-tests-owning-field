package wpi.owning.field;

import org.checkerframework.checker.calledmethods.qual.EnsuresCalledMethods;
import org.checkerframework.checker.mustcall.qual.MustCall;

@MustCall("finalizer") class App {
    private final Foo checkFieldsFoo;

    public App() {
        this.checkFieldsFoo = new Foo();
    }

    @EnsuresCalledMethods(
            value = {"this.checkFieldsFoo"},
            methods = {"a"})
    void finalizer() {
        this.checkFieldsFoo.a();
    }

    @MustCall("a") static class Foo {
        void a() {}

        void c() {}
    }

    Foo makeFoo() {
        return new Foo();
    }

    @MustCall("b") static class FooField {
        private final Foo finalOwningFoo;

        public FooField() {
            this.finalOwningFoo = new Foo();
        }

        public Foo returnAlias(Foo f) {
            Foo ff = f;
            return ff;
        }

        public void owningFoo(Foo f) {
            Foo ff = returnAlias(f);
            ff.a();
        }

        public void owningFooTest() {
            Foo f = new Foo();
            owningFoo(f);
        }

        @EnsuresCalledMethods(
                value = {"this.finalOwningFoo"},
                methods = {"a"})
        void b() {
            this.finalOwningFoo.a();
            this.finalOwningFoo.c();
        }
    }
}

