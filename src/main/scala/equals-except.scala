import shapeless._
object equalsExcept {

  import labelled._
  import ops.record._

  trait EqualsExcept[T] {
    def equalsExcept(t1: T, t2: T, fields: Symbol*): Boolean
  }

  trait LowPriorityEqualsExcept {
    implicit def catchAll[A] = new EqualsExcept[A] {
      def equalsExcept(x: A, y: A, fields: Symbol*): Boolean = {
        x == y
      }
    }

  }

  object EqualsExcept extends LowPriorityEqualsExcept {

    implicit def generic[T, R](implicit
      gen: LabelledGeneric.Aux[T, R],
      eqRepr: Lazy[EqualsExcept[R]]): EqualsExcept[T] =
      new EqualsExcept[T] {
        def equalsExcept(x: T, y: T, fields: Symbol*): Boolean =
          eqRepr.value.equalsExcept(gen.to(x), gen.to(y), fields: _*)
      }

    implicit val hnil: EqualsExcept[HNil] = new EqualsExcept[HNil] {
      def equalsExcept(x: HNil, y: HNil, fields: Symbol*): Boolean = true
    }

    implicit def product[K <: Symbol, H, T <: HList](implicit
      key: Witness.Aux[K],
      eqH: Lazy[EqualsExcept[H]],
      eqT: Lazy[EqualsExcept[T]]): EqualsExcept[FieldType[K, H] :: T] =
      new EqualsExcept[FieldType[K, H] :: T] {
        def equalsExcept(x: FieldType[K, H] :: T, y: FieldType[K, H] :: T, fields: Symbol*): Boolean = {
          val current: Symbol = key.value
          if (fields.contains(current)) true
          else eqH.value.equalsExcept(x.head, y.head, fields: _*) && eqT.value.equalsExcept(x.tail, y.tail, fields: _*)
        }
      }

    implicit val cnil: EqualsExcept[CNil] = new EqualsExcept[CNil] {
      def equalsExcept(x: CNil, y: CNil, fields: Symbol*): Boolean = true
    }

    implicit def coproduct[H, T <: Coproduct, K <: Symbol](implicit
      key: Witness.Aux[K],
      eqH: Lazy[EqualsExcept[H]],
      eqT: Lazy[EqualsExcept[T]]): EqualsExcept[FieldType[K, H] :+: T] =
      new EqualsExcept[FieldType[K, H] :+: T] {
        def equalsExcept(x: FieldType[K, H] :+: T, y: FieldType[K, H] :+: T, fields: Symbol*): Boolean = {
          (x, y) match {
            case (Inl(xh), Inl(yh)) => eqH.value.equalsExcept(xh, yh, fields: _*)
            case (Inr(xt), Inr(yt)) => eqT.value.equalsExcept(xt, yt, fields: _*)
            case _ => false
          }
        }
      }
  }

  implicit class EqualsExceptOps[T](x: T)(implicit eqT: EqualsExcept[T]) {
    def equalsExcept(y: T, fields: Symbol*): Boolean = eqT.equalsExcept(x, y, fields: _*)
  }

}
