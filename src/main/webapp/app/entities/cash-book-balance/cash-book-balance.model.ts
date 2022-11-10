import { IMerchant } from 'app/entities/merchant/merchant.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';

export interface ICashBookBalance {
  id: number;
  balance?: number | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
}

export type NewCashBookBalance = Omit<ICashBookBalance, 'id'> & { id: null };
