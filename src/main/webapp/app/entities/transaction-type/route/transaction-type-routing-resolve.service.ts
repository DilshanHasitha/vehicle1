import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionType } from '../transaction-type.model';
import { TransactionTypeService } from '../service/transaction-type.service';

@Injectable({ providedIn: 'root' })
export class TransactionTypeRoutingResolveService implements Resolve<ITransactionType | null> {
  constructor(protected service: TransactionTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transactionType: HttpResponse<ITransactionType>) => {
          if (transactionType.body) {
            return of(transactionType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
