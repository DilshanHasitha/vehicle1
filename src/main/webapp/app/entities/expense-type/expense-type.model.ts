export interface IExpenseType {
  id: number;
  code?: string | null;
  name?: string | null;
  isActive?: boolean | null;
}

export type NewExpenseType = Omit<IExpenseType, 'id'> & { id: null };
