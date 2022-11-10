import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';

@Injectable({ providedIn: 'root' })
export class ExpenseRoutingResolveService implements Resolve<IExpense | null> {
  constructor(protected service: ExpenseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpense | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expense: HttpResponse<IExpense>) => {
          if (expense.body) {
            return of(expense.body);
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
