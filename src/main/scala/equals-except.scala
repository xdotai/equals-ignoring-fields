import shapeless._
object equalsExcept {

  import labelled._
  import ops.record._
  import ops.hlist.Prepend

  trait EqualsExcept[T] {
    def equalsExcept(t1: T, t2: T, field: Witness.Aux[Symbol]): Boolean
  }

  object EqualsExcept {
    implicit def generic[T, G](implicit gen: LabelledGeneric.Aux[T, G], sg: Lazy[EqualsExcept[G]]): EqualsExcept[T] =
      new EqualsExcept[T] {
        type Out = R
        def equalsExcept(f: F, field: Witness.Aux[Symbol]) = sg.value.equalsExcept(gen.to(f), field)
      }

    implicit def product: EqualsExcept[HNil, HNil] =
      new EqualsExcept[HNil] {
        type Out = HNil
        def equalsExcept(p: HNil, field: Witness.Aux[Symbol]) = HNil
      }

    implicit def product[K <: Symbol, V, T <: HList, RV <: HList, RT <: HList, R <: HList](implicit
      key: Witness.Aux[K],
      selector: Selector.Aux[FieldType[K, V] :: T, K, Symbol],
      sv: Lazy[EqualsExcept[V, RV]],
      st: Lazy[EqualsExcept[T, RT]],
      prepend: Prepend.Aux[RV, RT, R]): EqualsExcept[FieldType[K, V] :: T, R] =
      new EqualsExcept[FieldType[K, V] :: T] {
        type Out = R
        def equalsExcept(p: FieldType[K, V] :: T, field: Witness.Aux[Symbol]) = {
          val currentField: Symbol = selector(p)
          val fieldToRemove: Symbol = field.value
          if (currentField == fieldToRemove) sv.value.equalsExcept(p.head, field)
          else p.head :: st.value.equalsExcept(p.tail, field)
        }
      }

    implicit def cnil: EqualsExcept[CNil, HNil] =
      new EqualsExcept[CNil] {
        type Out = HNil
        def equalsExcept(p: CNil, field: Witness.Aux[Symbol]) = HNil
      }

    implicit def coproduct[K <: Symbol, V, T <: Coproduct, RV <: HList, RT <: HList, R <: HList](implicit
      key: Witness.Aux[K],
      sv: Lazy[EqualsExcept[V, RV]],
      st: Lazy[EqualsExcept[T, RT]],
      prepend: Prepend.Aux[RV, RT, R]): EqualsExcept[FieldType[K, V] :+: T, R] =
      new EqualsExcept[FieldType[K, V] :+: T] {
        type Out = R
        def equalsExcept(c: FieldType[K, V] :+: T, field: Witness.Aux[Symbol]): HList =
          c match {
            case Inl(head) => sv.value.equalsExcept(head, field)
            case Inr(tail) => st.value.equalsExcept(tail, field)
          }
      }
  }

  implicit class EqualsExceptOps[T](x: T)(implicit equalsExceptT: EqualsExcept[T]) {
    def equalsExcept(field: Witness.Aux[Symbol]): HList = equalsExceptT.equalsExcept(x, field)
  }

  sealed trait Animal
  case class Cat(name: String, fish: Int) extends Animal
  case class Dog(name: String, bones: Int) extends Animal

  val felix: Cat = Cat("Felix", 1)
  val tigger = Dog("Tigger", 2)
}

