package equalsIgnoringFields
import shapeless._
import labelled._
import ops.record._

trait EqualsIgnoringFields[T] {
  def equalsIgnoringFields(t1: T, t2: T, fields: Symbol*): Boolean
}

trait LowPriorityEqualsIgnoringFields {
  implicit def catchAll[A] = new EqualsIgnoringFields[A] {
    def equalsIgnoringFields(x: A, y: A, fields: Symbol*): Boolean = {
      x == y
    }
  }

}

object EqualsIgnoringFields extends LowPriorityEqualsIgnoringFields {

  implicit def generic[T, R](implicit
    gen: LabelledGeneric.Aux[T, R],
    eqRepr: Lazy[EqualsIgnoringFields[R]]): EqualsIgnoringFields[T] =
    new EqualsIgnoringFields[T] {
      def equalsIgnoringFields(x: T, y: T, fields: Symbol*): Boolean =
        eqRepr.value.equalsIgnoringFields(gen.to(x), gen.to(y), fields: _*)
    }

  implicit val hnil: EqualsIgnoringFields[HNil] = new EqualsIgnoringFields[HNil] {
    def equalsIgnoringFields(x: HNil, y: HNil, fields: Symbol*): Boolean = true
  }

  implicit def product[K <: Symbol, H, T <: HList](implicit
    key: Witness.Aux[K],
    eqH: Lazy[EqualsIgnoringFields[H]],
    eqT: Lazy[EqualsIgnoringFields[T]]): EqualsIgnoringFields[FieldType[K, H] :: T] =
    new EqualsIgnoringFields[FieldType[K, H] :: T] {
      def equalsIgnoringFields(x: FieldType[K, H] :: T, y: FieldType[K, H] :: T, fields: Symbol*): Boolean = {
        val current: Symbol = key.value
        if (fields.contains(current)) eqT.value.equalsIgnoringFields(x.tail, y.tail, fields: _*)
        else eqH.value.equalsIgnoringFields(x.head, y.head, fields: _*) && eqT.value.equalsIgnoringFields(x.tail, y.tail, fields: _*)
      }
    }

  implicit val cnil: EqualsIgnoringFields[CNil] = new EqualsIgnoringFields[CNil] {
    def equalsIgnoringFields(x: CNil, y: CNil, fields: Symbol*): Boolean = true
  }

  implicit def coproduct[H, T <: Coproduct, K <: Symbol](implicit
    key: Witness.Aux[K],
    eqH: Lazy[EqualsIgnoringFields[H]],
    eqT: Lazy[EqualsIgnoringFields[T]]): EqualsIgnoringFields[FieldType[K, H] :+: T] =
    new EqualsIgnoringFields[FieldType[K, H] :+: T] {
      def equalsIgnoringFields(x: FieldType[K, H] :+: T, y: FieldType[K, H] :+: T, fields: Symbol*): Boolean = {
        (x, y) match {
          case (Inl(xh), Inl(yh)) => eqH.value.equalsIgnoringFields(xh, yh, fields: _*)
          case (Inr(xt), Inr(yt)) => eqT.value.equalsIgnoringFields(xt, yt, fields: _*)
          case _ => false
        }
      }
    }
}

object syntax {
  implicit class EqualsIgnoringFieldsOps[T](x: T)(implicit eqT: EqualsIgnoringFields[T]) {
    def equalsIgnoringFields(fields: Symbol*)(y: T): Boolean = eqT.equalsIgnoringFields(x, y, fields: _*)
  }

}
