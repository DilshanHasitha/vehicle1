import dayjs from 'dayjs/esm';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';

export interface ICashBook {
  id: number;
  transactionDate?: dayjs.Dayjs | null;
  transactionDescription?: string | null;
  transactionAmountDR?: number | null;
  transactionAmountCR?: number | null;
  transactionBalance?: number | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
}

export type NewCashBook = Omit<ICashBook, 'id'> & { id: null };
