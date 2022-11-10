export interface IExpense {
  id: number;
  expenseCode?: string | null;
  expenseName?: string | null;
  expenseLimit?: number | null;
  isActive?: boolean | null;
}

export type NewExpense = Omit<IExpense, 'id'> & { id: null };
