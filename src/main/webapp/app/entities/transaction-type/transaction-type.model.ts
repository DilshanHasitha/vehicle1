export interface ITransactionType {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
}

export type NewTransactionType = Omit<ITransactionType, 'id'> & { id: null };
