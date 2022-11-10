import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICashBook, NewCashBook } from '../cash-book.model';

export type PartialUpdateCashBook = Partial<ICashBook> & Pick<ICashBook, 'id'>;

type RestOf<T extends ICashBook | NewCashBook> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

export type RestCashBook = RestOf<ICashBook>;

export type NewRestCashBook = RestOf<NewCashBook>;

export type PartialUpdateRestCashBook = RestOf<PartialUpdateCashBook>;

export type EntityResponseType = HttpResponse<ICashBook>;
export type EntityArrayResponseType = HttpResponse<ICashBook[]>;

@Injectable({ providedIn: 'root' })
export class CashBookService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cash-books');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cashBook: NewCashBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashBook);
    return this.http
      .post<RestCashBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cashBook: ICashBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashBook);
    return this.http
      .put<RestCashBook>(`${this.resourceUrl}/${this.getCashBookIdentifier(cashBook)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cashBook: PartialUpdateCashBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashBook);
    return this.http
      .patch<RestCashBook>(`${this.resourceUrl}/${this.getCashBookIdentifier(cashBook)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCashBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCashBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCashBookIdentifier(cashBook: Pick<ICashBook, 'id'>): number {
    return cashBook.id;
  }

  compareCashBook(o1: Pick<ICashBook, 'id'> | null, o2: Pick<ICashBook, 'id'> | null): boolean {
    return o1 && o2 ? this.getCashBookIdentifier(o1) === this.getCashBookIdentifier(o2) : o1 === o2;
  }

  addCashBookToCollectionIfMissing<Type extends Pick<ICashBook, 'id'>>(
    cashBookCollection: Type[],
    ...cashBooksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cashBooks: Type[] = cashBooksToCheck.filter(isPresent);
    if (cashBooks.length > 0) {
      const cashBookCollectionIdentifiers = cashBookCollection.map(cashBookItem => this.getCashBookIdentifier(cashBookItem)!);
      const cashBooksToAdd = cashBooks.filter(cashBookItem => {
        const cashBookIdentifier = this.getCashBookIdentifier(cashBookItem);
        if (cashBookCollectionIdentifiers.includes(cashBookIdentifier)) {
          return false;
        }
        cashBookCollectionIdentifiers.push(cashBookIdentifier);
        return true;
      });
      return [...cashBooksToAdd, ...cashBookCollection];
    }
    return cashBookCollection;
  }

  protected convertDateFromClient<T extends ICashBook | NewCashBook | PartialUpdateCashBook>(cashBook: T): RestOf<T> {
    return {
      ...cashBook,
      transactionDate: cashBook.transactionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCashBook: RestCashBook): ICashBook {
    return {
      ...restCashBook,
      transactionDate: restCashBook.transactionDate ? dayjs(restCashBook.transactionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCashBook>): HttpResponse<ICashBook> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCashBook[]>): HttpResponse<ICashBook[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
