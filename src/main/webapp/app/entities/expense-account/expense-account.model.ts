import dayjs from 'dayjs/esm';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { IExpense } from 'app/entities/expense/expense.model';

export interface IExpenseAccount {
  id: number;
  transactionDate?: dayjs.Dayjs | null;
  transactionDescription?: string | null;
  transactionAmountDR?: number | null;
  transactionAmountCR?: number | null;
  transactionBalance?: number | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  expense?: Pick<IExpense, 'id'> | null;
}

export type NewExpenseAccount = Omit<IExpenseAccount, 'id'> & { id: null };
