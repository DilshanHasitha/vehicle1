import { IExpense } from 'app/entities/expense/expense.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';

export interface IExpenseAccountBalance {
  id: number;
  balance?: number | null;
  expense?: Pick<IExpense, 'id'> | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
}

export type NewExpenseAccountBalance = Omit<IExpenseAccountBalance, 'id'> & { id: null };
